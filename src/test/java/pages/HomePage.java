package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // LOCATORS

    private By signInLink =
            By.cssSelector("a[title='Đăng nhập tài khoản khách hàng của bạn']");

    private By logoutLink =
            By.cssSelector("a.logout");

    private By searchInput =
            By.cssSelector("#search_widget input[name='s']");

    private By productImageLink =
            By.cssSelector(".product-miniature a.product-thumbnail");

    private By cartLink = By.cssSelector("#_desktop_cart a");
    private By homeLogo = By.cssSelector("#_desktop_logo a");

    // CONSTRUCTOR
    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ACTIONS
    public void goToHomePage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(homeLogo)
        ).click();
    }

    public void openLoginPage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(signInLink)
        ).click();
    }

    public void logout() {
        wait.until(
                ExpectedConditions.elementToBeClickable(logoutLink)
        ).click();
    }

    public void searchProduct(String keyword) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput)
        );
        input.clear();
        input.sendKeys(keyword, Keys.ENTER);
    }

    public void openProductByImage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(productImageLink)
        ).click();
    }

    public void openCartPage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(cartLink)
        ).click();
    }

    // CHECK STATUS
    public boolean isLoggedIn() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(logoutLink)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSignInLinkDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(signInLink)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoggedOut() {
        try {
            wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(logoutLink)
            );
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(signInLink)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isHomePageDisplayed() {
        try {
            wait.until(
                    ExpectedConditions.urlToBe("http://localhost:8080/")
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}