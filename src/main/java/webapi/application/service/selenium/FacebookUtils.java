package webapi.application.service.selenium;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import webapi.domain.FacebookComment;
import webapi.domain.FacebookPost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FacebookUtils {

    public static void loadAllComments(WebDriver driver, FacebookPost post) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 👉 Scroll để mở hết bình luận nếu có nút "Xem thêm"
            while (true) {
                try {
                    WebElement moreButton = driver.findElement(By.xpath(
                            "//div[@role='button' and .//span[contains(text(), 'Xem thêm') or contains(text(), 'View more')]]"
                    ));
                    moreButton.click();
                    Thread.sleep(1500);
                } catch (Exception e) {
                    break; // Không còn nút "Xem thêm"
                }
            }

            // ✅ Lấy tất cả container chứa bình luận
            List<WebElement> commentContainers = new ArrayList<>();

            // 1. Bình luận trong group
            commentContainers.addAll(
                    driver.findElements(By.xpath("//div[@aria-label='Bình luận' or contains(@aria-label,'comment')]"))
            );

            // 2. Bình luận trong feed (fallback)
            commentContainers.addAll(
                    driver.findElements(By.cssSelector("div.x1vvkbs"))
            );

            Set<String> seenComments = new HashSet<>();

            for (WebElement container : commentContainers) {
                String commenterName = container.findElements(By.xpath(".//span[contains(@class, 'x1xmvt09')]"))
                        .stream()
                        .map(WebElement::getText)
                        .filter(text -> !text.isBlank())
                        .findFirst()
                        .orElse(null);

                String commentText = container.findElements(By.xpath(".//div[@dir='auto']"))
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
                    seenComments.add(commentText);
                    System.out.println("💬 Bình luận hợp lệ: " + commentText + " 👤 " + commenterName);
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Không lấy được danh sách bình luận: " + e.getMessage());
        }
    }


}
