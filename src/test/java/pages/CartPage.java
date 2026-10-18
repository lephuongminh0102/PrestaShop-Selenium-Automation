package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    private WebDriverWait wait;

    // LOCATORS
    private By checkoutLink =
            By.cssSelector(".cart-detailed-actions a.btn.btn-primary");

    private By cartItems =
            By.cssSelector(".cart-overview .cart-item");

    // CONSTRUCTOR
    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
    }

    // ACTIONS
    public void clickCheckoutLink() {
        wait.until(
                ExpectedConditions.elementToBeClickable(checkoutLink)
        ).click();
    }

    // CHECK STATUS
    public boolean isCheckoutLinkDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(checkoutLink)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasProduct() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(cartItems)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}