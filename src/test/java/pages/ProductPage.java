package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // LOCATORS
    private By addToCartButton =
            By.cssSelector("button[data-button-action='add-to-cart']");

    private By cartPopup =
            By.id("blockcart-modal");

    private By continueShoppingButton =
            By.cssSelector("#blockcart-modal button[data-dismiss='modal']");

    private By quantityInput =
            By.id("quantity_wanted");

    private By alertMessage =
            By.cssSelector(".alert.alert-warning, .alert.alert-danger, #blockcart-modal");

    private By productAvailability =
            By.id("product-availability");

    // Dựa trên HTML popup đã kiểm tra trước đó
    private By checkoutLink =
            By.cssSelector(
                    "#blockcart-modal .modal-body a.btn.btn-primary[href*='giỏ hàng']"
            );

    // CONSTRUCTOR
    public ProductPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ACTIONS
    public void clickAddToCart() {
        wait.until(
                ExpectedConditions.elementToBeClickable(addToCartButton)
        ).click();
    }

    public void clickContinueShopping() {
        wait.until(
                ExpectedConditions.elementToBeClickable(continueShoppingButton)
        ).click();
    }

    public void clickCheckoutLink() {
        wait.until(
                ExpectedConditions.elementToBeClickable(checkoutLink)
        ).click();
    }

    public void setQuantity(String quantity) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(quantityInput)
        );
        input.clear();

        // Dùng JavaScript để ép giá trị (bỏ qua kiểm tra ràng buộc min/max của trình duyệt)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", input, quantity);
    }

    // CHECK STATUS
    public boolean isProductPageDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(addToCartButton)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isAddToCartButtonEnabled() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(addToCartButton)
            ).isEnabled();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isAddToCartPopupDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(cartPopup)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getAvailabilityMessage() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(productAvailability)
            ).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
}