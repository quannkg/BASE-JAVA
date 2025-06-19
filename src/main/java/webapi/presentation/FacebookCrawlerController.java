package webapi.presentation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
