package webapi.application.service.selenium;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import webapi.application.service.selenium.dto.request.FacebookCommentRequest;
import webapi.application.service.selenium.dto.request.FacebookPostRequest;
import webapi.application.service.selenium.dto.response.FacebookCommentDto;
import webapi.application.service.selenium.dto.response.FacebookCommentResponse;
import webapi.application.service.selenium.dto.response.FacebookPostDto;
import webapi.application.service.selenium.dto.response.FacebookPostResponse;
import webapi.application.service.selenium.interfaces.IFacebookCrawler;
import webapi.domain.FacebookComment;
import webapi.domain.FacebookPost;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.helper.ModelMapperUtils;
import webapi.infrastructure.repositories.FacebookCommentRepository;
import webapi.infrastructure.repositories.FacebookPostRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class FacebookSeleniumService implements IFacebookCrawler {
    private final FacebookPostRepository facebookPostRepository;
    private final FacebookCommentRepository facebookCommentRepository;
    private final GeminiSentimentAnalyzerService geminiSentimentAnalyzerService;

    @Autowired
    private FacebookSessionManager facebookSessionManager;

    public void crawlPostContent(String postUrl) {
        WebDriver driver = facebookSessionManager.getLoggedInDriver();

        try {
            FacebookSessionManager.ensureLogin(driver);
            driver.get(postUrl);
            Thread.sleep(8000);

            FacebookPost post = new FacebookPost();
            post.setPostUrl(postUrl);
            Thread.sleep(8000);
            FacebookUtils.loadAllComments(driver,post);
            // ===== 1. Nội dung bài viết =====
            try {
                WebElement postContent = driver.findElement(By.xpath("//div[@data-ad-preview='message']"));
                post.setContent(postContent.getText());
                System.out.println("✅ Nội dung bài viết:\n" + post.getContent());
            } catch (Exception e) {
                System.out.println("⚠️ Không tìm thấy nội dung bài viết");
            }

            // ===== 2. Ảnh trong bài =====
            try {
                WebElement image = driver.findElement(By.xpath("//img[contains(@src, 'scontent')]"));
                post.setImageUrl(image.getAttribute("src"));
                System.out.println("🖼️ Link ảnh: " + post.getImageUrl());
            } catch (Exception e) {
                System.out.println("⚠️ Không tìm thấy ảnh trong bài");
            }

            // ===== 3. Danh sách bình luận =====
            try {
                List<WebElement> containers = driver.findElements(By.cssSelector("div.x1vvkbs"));
                Set<String> seenComments = new HashSet<>();

                for (WebElement container : containers) {
                    String commenterName = container.findElements(By.cssSelector("span.x1xmvt09"))
                            .stream()
                            .map(WebElement::getText)
                            .filter(text -> !text.isBlank())
                            .findFirst()
                            .orElse(null);

                    String commentText = container.findElements(By.cssSelector("div[dir='auto']"))
                            .stream()
                            .map(WebElement::getText)
                            .filter(text -> !text.isBlank())
                            .filter(text -> commenterName == null || !text.equals(commenterName))
                            .findFirst()
                            .orElse(null);

                    if (commentText != null && !seenComments.contains(commentText)) {
                        FacebookComment comment = new FacebookComment();
                        comment.setCommenterName(commenterName);
                        comment.setCommentText(commentText);
                        post.addComment(comment);
                        System.out.println("💬 Bình luận hợp lệ: " + commentText + " 👤 " + commenterName);
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Không lấy được danh sách bình luận: " + e.getMessage());
            }

            // ===== 4. Phân tích cảm xúc =====
            geminiSentimentAnalyzerService.analyzeAllCommentsInBatch(post);

            facebookPostRepository.save(post);
            System.out.println("✅ Đã lưu bài viết và toàn bộ bình luận");

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi cào dữ liệu: " + e.getMessage());
        } finally {
            // Nếu không cần reuse Chrome cho lần sau, thì giữ lại dòng này:
            driver.quit();
        }
    }

    @Override
    public FacebookPostResponse searchPosts(FacebookPostRequest request) {
        Page<FacebookPost> entity =  facebookPostRepository.search(request, request.getPageable());
        List<FacebookPostDto> data = ModelMapperUtils.mapList(entity.getContent().stream().filter(Objects::nonNull).toList(), FacebookPostDto.class);
        return FacebookPostResponse.builder()
                .data(data)
                .count(entity.getNumberOfElements())
                .total(entity.getTotalElements())
                .build();
    }


    @Override
    public FacebookPostDto getPostById(Long postId) {
        FacebookPost facebookPost = facebookPostRepository.findById(postId)
                .orElseThrow(() -> new AppException("Không tìm thấy quyền với id: " + postId, HttpStatus.NOT_FOUND));
        return FacebookPostDto.builder()
                .id(facebookPost.getId())
                .content(facebookPost.getContent())
                .postUrl(facebookPost.getPostUrl())
                .imageUrl(facebookPost.getImageUrl())
                .createdAt(facebookPost.getCreatedAt())
                .build();
    }

    @Override
    public FacebookCommentResponse searchComments(FacebookCommentRequest request) {
        Page<FacebookComment> entity =  facebookCommentRepository.search(request, request.getPageable());
        List<FacebookCommentDto> data = ModelMapperUtils.mapList(entity.getContent().stream().filter(Objects::nonNull).toList(), FacebookCommentDto.class);
        return FacebookCommentResponse.builder()
                .data(data)
                .count(entity.getNumberOfElements())
                .total(entity.getTotalElements())
                .build();
    }

    @Override
    public FacebookCommentDto getCommentById(Long postId) {
        FacebookComment facebookComment = facebookCommentRepository.findById(postId)
                .orElseThrow(() -> new AppException("Không tìm thấy quyền với id: " + postId, HttpStatus.NOT_FOUND));
        return FacebookCommentDto.builder()
                .id(facebookComment.getId())
                .commenterName(facebookComment.getCommenterName())
                .commentText(facebookComment.getCommentText())
                .positive(facebookComment.getPositive())
                .negative(facebookComment.getNegative())
                .neutral(facebookComment.getNeutral())
                .build();
    }
}
