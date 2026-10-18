package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final String BASE_URL = "http://localhost:8080";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Đặt ngôn ngữ ưu tiên của Chrome là tiếng Việt
        options.addArguments("--lang=vi");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Chờ tải trang tối đa 30 giây
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void printResult(String checkpoint, String expected, String actual) {
        System.out.println("[" + checkpoint + "]");
        System.out.println("   Mong đợi : " + expected);
        System.out.println("   Thực tế  : " + actual);
    }
}