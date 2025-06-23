package webapi.presentation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapi.application.service.selenium.dto.request.FacebookCommentRequest;
import webapi.application.service.selenium.dto.request.FacebookPostRequest;
import webapi.application.service.selenium.interfaces.IFacebookCrawler;

@RestController
@RequestMapping("/api/facebook")
@AllArgsConstructor
public class FacebookCrawlerController {
    private final IFacebookCrawler iFacebookCrawler;

    @GetMapping("/crawl")
    public ResponseEntity<String> crawlFacebookPost(@RequestParam String postUrl) {
         iFacebookCrawler.crawlPostContent(postUrl);
        return ResponseEntity.ok("content");
    }
    @PostMapping("/search-posts")
    public ResponseEntity<?> searchPosts(@RequestBody FacebookPostRequest request) {
        return ResponseEntity.ok(iFacebookCrawler.searchPosts(request));
    }
    @GetMapping("/get-post")
    public ResponseEntity<?> getPostById(@RequestParam Long postId) {
        return ResponseEntity.ok(iFacebookCrawler.getPostById(postId));
    }
    @PostMapping("/search-comments")
    public ResponseEntity<?> searchComments(@RequestBody FacebookCommentRequest request) {
        return ResponseEntity.ok(iFacebookCrawler.searchComments(request));
    }
    @GetMapping("/get-comment")
    public ResponseEntity<?> getCommentById(@RequestParam Long commentId) {
        return ResponseEntity.ok(iFacebookCrawler.getCommentById(commentId));
    }
}
