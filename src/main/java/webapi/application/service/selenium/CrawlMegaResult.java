package webapi.application.service.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import webapi.application.service.selenium.interfaces.IVietLottCrawler;
import webapi.application.service.vietlott.LotteResultService;
import webapi.domain.LotteResult;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CrawlMegaResult implements IVietLottCrawler {

    private final LotteResultService lotteResultService;

    @Override
    public void crawlVietLott645Result() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/winning-number-645");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            int currentPage = 1;

            while (true) {
                System.out.println("🔎 Đang xử lý trang: " + currentPage);

                // Chờ bảng dữ liệu hiện ra
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#divResultContent table.table-hover tbody tr")));

                List<WebElement> rows = driver.findElements(
                        By.cssSelector("#divResultContent table.table-hover tbody tr"));
                System.out.println("📌 Số dòng tìm được: " + rows.size());

                for (WebElement row : rows) {
                    try {
                        List<WebElement> cols = row.findElements(By.tagName("td"));
                        if (cols.size() < 3) continue;

                        String dateStr = cols.get(0).getText().trim();     // dd/MM/yyyy
                        String kyQuay = cols.get(1).getText().trim();

                        List<WebElement> numberSpans = cols.get(2).findElements(By.cssSelector("span.bong_tron"));
                        List<String> numbers = numberSpans.stream()
                                .map(WebElement::getText)
                                .filter(s -> !s.isBlank())
                                .collect(Collectors.toList());

                        if (numbers.size() < 6) {
                            System.out.println("⚠️ Dữ liệu thiếu số, bỏ qua kỳ: " + kyQuay);
                            continue;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate localDate = LocalDate.parse(dateStr, formatter);
                        Instant ngayMoThuong = localDate.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();

                        LotteResult result = new LotteResult();
                        result.setLoaiVeSo("MEGA_6_45");
                        result.setKyQuay(kyQuay);
                        result.setNgayMoThuong(ngayMoThuong);
                        result.setDaySoTrungThuong(String.join(",", numbers));
                        result.setJackpot(null); // Có thể update nếu cần lấy Jackpot sau

                        lotteResultService.saveResult(result);
                    } catch (Exception e) {
                        System.out.println("⚠️ Lỗi xử lý dòng: " + e.getMessage());
                    }
                }

                // Tìm nút next trang
                try {
                    WebElement nextBtn = driver.findElement(
                            By.cssSelector("#divResultContent > div > div > ul > li:nth-child(7) > a"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
                    System.out.println("👉 Đã nhấn nút Next");
                    Thread.sleep(2000);
                    currentPage++;
                } catch (NoSuchElementException e) {
                    System.out.println("✅ Không còn nút Next → kết thúc crawl.");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Lỗi tổng thể: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @Override
    public void crawlVietLott655Result() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/winning-number-655");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            int currentPage = 1;

            while (true) {
                System.out.println("🔎 Đang xử lý trang: " + currentPage);

                // Chờ bảng dữ liệu hiện ra
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("#divResultContent table.table-hover tbody tr")));

                List<WebElement> rows = driver.findElements(
                        By.cssSelector("#divResultContent table.table-hover tbody tr"));
                System.out.println("📌 Số dòng tìm được: " + rows.size());

                for (WebElement row : rows) {
                    try {
                        List<WebElement> cols = row.findElements(By.tagName("td"));
                        if (cols.size() < 3) continue;

                        String dateStr = cols.get(0).getText().trim();     // dd/MM/yyyy
                        String kyQuay = cols.get(1).getText().trim();

                        List<WebElement> numberSpans = cols.get(2).findElements(By.cssSelector("span.bong_tron"));
                        List<String> numbers = numberSpans.stream()
                                .map(WebElement::getText)
                                .filter(s -> !s.isBlank())
                                .collect(Collectors.toList());

                        if (numbers.size() < 6) {
                            System.out.println("⚠️ Dữ liệu thiếu số, bỏ qua kỳ: " + kyQuay);
                            continue;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate localDate = LocalDate.parse(dateStr, formatter);
                        Instant ngayMoThuong = localDate.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();

                        LotteResult result = new LotteResult();
                        result.setLoaiVeSo("POWER_6_55");
                        result.setKyQuay(kyQuay);
                        result.setNgayMoThuong(ngayMoThuong);
                        result.setDaySoTrungThuong(String.join(",", numbers));
                        result.setJackpot(null); // Có thể update nếu cần lấy Jackpot sau

                        lotteResultService.saveResult(result);
                    } catch (Exception e) {
                        System.out.println("⚠️ Lỗi xử lý dòng: " + e.getMessage());
                    }
                }

                // Tìm nút next trang
                try {
                    WebElement nextBtn = driver.findElement(
                            By.cssSelector("#divResultContent > div > div > ul > li:nth-child(7) > a"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
                    System.out.println("👉 Đã nhấn nút Next");
                    Thread.sleep(2000);
                    currentPage++;
                } catch (NoSuchElementException e) {
                    System.out.println("✅ Không còn nút Next → kết thúc crawl.");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Lỗi tổng thể: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }


}

