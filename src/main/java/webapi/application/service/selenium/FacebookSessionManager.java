package webapi.application.service.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FacebookSessionManager {
    private static final String FB_EMAIL = "email";
    private static final String FB_PASSWORD = "pass";
    private static final String PROFILE_PATH = "C:/selenium/facebook-session"; // 👉 nên để cấu hình riêng
    private static final String FACEBOOK_URL = "https://www.facebook.com/";

    public WebDriver getLoggedInDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + PROFILE_PATH);
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.facebook.com/");

        if (isFirstTimeLogin(driver)) {
            waitForManualLogin(driver);
        }

        return driver;
    }

    private boolean isFirstTimeLogin(WebDriver driver) {
        driver.get("https://www.facebook.com");

        try {
            Thread.sleep(10000);
            List<WebElement> emailFields = driver.findElements(By.id("email"));
            List<WebElement> passFields = driver.findElements(By.id("pass"));

            return !emailFields.isEmpty() && !passFields.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }


    private void waitForManualLogin(WebDriver driver) {
        System.out.println("⚠️ Vui lòng đăng nhập Facebook thủ công. Đợi bạn hoàn tất...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(3));

        try {
            wait.until(d -> !isFirstTimeLogin(d));
            System.out.println("✅ Đăng nhập thành công!");
        } catch (TimeoutException e) {
            throw new RuntimeException("⛔ Hết thời gian. Bạn chưa đăng nhập xong.");
        }
    }



    public static void ensureLogin(WebDriver driver) {
        driver.get("https://www.facebook.com/");
        try {
            Thread.sleep(3000);
            if (driver.getCurrentUrl().contains("login")) {
                throw new RuntimeException("⚠️ Session đã hết hạn. Vui lòng login lại bằng tay.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loginFacebook(WebDriver driver) throws InterruptedException {
        driver.get("https://www.facebook.com/login");
        Thread.sleep(2000);

        try {
            WebElement emailInput = driver.findElement(By.id("email"));
            emailInput.sendKeys(FB_EMAIL);

            WebElement passwordInput = driver.findElement(By.id("pass"));
            passwordInput.sendKeys(FB_PASSWORD);

            WebElement loginButton = driver.findElement(By.name("login"));
            loginButton.click();

            Thread.sleep(5000); // Đợi login xong

            if (driver.getCurrentUrl().contains("login")) {
                throw new RuntimeException("Đăng nhập thất bại, kiểm tra tài khoản hoặc captcha");
            }

            System.out.println("✅ Đăng nhập thành công");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đăng nhập Facebook: " + e.getMessage());
        }
    }
}
