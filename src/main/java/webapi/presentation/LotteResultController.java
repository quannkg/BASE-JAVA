package webapi.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webapi.application.service.selenium.interfaces.IVietLottCrawler;

@RestController
@RequestMapping("/api/lotte")
@RequiredArgsConstructor
public class LotteResultController {
    private final IVietLottCrawler vietLottCrawler;

    @GetMapping("/crawl/mega645")
    public ResponseEntity<?> crawlMega() {
        vietLottCrawler.crawlVietLott645Result();
        return ResponseEntity.ok("Đã crawl xong kết quả Mega 6/45");
    }
    @GetMapping("/crawl/power655")
    public ResponseEntity<?> crawlPower655() {
        vietLottCrawler.crawlVietLott655Result();
        return ResponseEntity.ok("Đã crawl xong kết quả Power 6/55");
    }
}
