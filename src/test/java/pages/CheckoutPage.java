
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // LOCATORS - THÔNG TIN CÁ NHÂN
    private By personalInformationStep = By.id("checkout-personal-information-step");

    // LOCATORS - ĐỊA CHỈ
    private By addressStep = By.id("checkout-addresses-step");
    private By city = By.id("field-city");
    private By address = By.id("field-address1");
    private By phone = By.id("field-phone");

    private By phoneErrors =
            By.xpath(
                    "//input[@id='field-phone']" +
                            "/ancestor::div[contains(@class,'js-input-column')][1]" +
                            "//div[contains(@class,'help-block')]" +
                            "//li[contains(@class,'alert-danger')]"
            );

    private By continueAddressButton =
            By.cssSelector("#checkout-addresses-step button[name='confirm-addresses']");

    // LOCATORS - VẬN CHUYỂN
    private By shippingStep = By.id("checkout-delivery-step");

    private By shippingMethodLabel =
            By.cssSelector("#checkout-delivery-step .delivery-option label");

    private By selectedShippingMethod =
            By.cssSelector("#checkout-delivery-step input[name^='delivery_option']:checked");

    private By continueShippingButton =
            By.cssSelector("#checkout-delivery-step button[name='confirmDeliveryOption']");

    // LOCATORS - THANH TOÁN
    private By paymentStep = By.id("checkout-payment-step");

    private By selectedPaymentMethod =
            By.cssSelector("#checkout-payment-step input[name='payment-option']:checked");

    private By cashOnDeliveryOption = By.id("payment-option-1");
    private By terms = By.id("conditions_to_approve[terms-and-conditions]");

    private By placeOrderButton =
            By.cssSelector("#payment-confirmation button[type='submit']");

    // LOCATORS - XÁC NHẬN ĐƠN HÀNG
    private By orderConfirmationTitle =
            By.cssSelector("#content-hook_order_confirmation h3.card-title");

    // CONSTRUCTOR
    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ACTIONS - ĐỊA CHỈ
    public void enterCity(String value) {
        enterText(city, value);
    }

    public void enterAddress(String value) {
        enterText(address, value);
    }

    public void enterPhone(String value) {
        enterText(phone, value);
    }

    public void clickContinueAddress() {
        wait.until(
                ExpectedConditions.elementToBeClickable(continueAddressButton)
        ).click();
    }

    // ACTIONS - VẬN CHUYỂN
    public void selectShippingMethod() {
        if (!driver.findElements(selectedShippingMethod).isEmpty()) {
            return;
        }
        wait.until(
                ExpectedConditions.elementToBeClickable(shippingMethodLabel)
        ).click();
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        selectedShippingMethod
                )
        );
    }

    public void clickContinueShipping() {
        wait.until(
                ExpectedConditions.elementToBeClickable(continueShippingButton)
        ).click();
    }

    // ACTIONS - THANH TOÁN
    public void selectCashOnDelivery() {
        WebElement option = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        cashOnDeliveryOption
                )
        );
        if (option.isSelected()) {
            return;
        }
        // Radio bị ẩn nên click nhãn tương ứng.
        By label = By.cssSelector(
                "label[for='payment-option-1']"
        );
        wait.until(
                ExpectedConditions.elementToBeClickable(label)
        ).click();
        wait.until(
                ExpectedConditions.elementToBeSelected(option)
        );
    }

    public void selectTerms() {
        WebElement checkbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(terms)
        );
        if (checkbox.isSelected()) {
            return;
        }
        // Click checkbox, tránh click nhầm liên kết điều khoản.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkbox);
        wait.until(
                ExpectedConditions.elementToBeSelected(checkbox)
        );
    }

    public void clickPlaceOrder() {
        wait.until(
                ExpectedConditions.elementToBeClickable(placeOrderButton)
        ).click();
    }

    // CHECK STATUS - THÔNG TIN CÁ NHÂN
    public boolean isPersonalInformationStepOpened() {
        return isStepOpened(personalInformationStep);
    }

    // CHECK STATUS - ĐỊA CHỈ
    public boolean isAddressStepOpened() {
        return isStepOpened(addressStep);
    }

    public boolean isAddressFormDisplayed() {
        return isElementDisplayed(city)
                && isElementDisplayed(address)
                && isElementDisplayed(phone);
    }

    public String getCityValidationMessage() {
        return getValidationMessage(city);
    }

    public String getAddressValidationMessage() {
        return getValidationMessage(address);
    }

    public String getPhoneValidationMessage() {
        return getValidationMessage(phone);
    }

    public String getPhoneErrors() {
        try {
            List<WebElement> errors = driver.findElements(phoneErrors);
            StringBuilder result = new StringBuilder();

            for (WebElement error : errors) {
                String message = error.getText().trim();

                if (!message.isEmpty()) {
                    if (result.length() > 0) {
                        result.append(" | ");
                    }

                    result.append(message);
                }
            }

            return result.toString();

        } catch (Exception e) {
            return "";
        }
    }

    // CHECK STATUS - VẬN CHUYỂN
    public boolean isShippingStepOpened() {
        return isStepOpened(shippingStep);
    }

    public boolean isShippingMethodSelected() {
        return !driver.findElements(selectedShippingMethod).isEmpty();
    }

    // CHECK STATUS - THANH TOÁN
    public boolean isPaymentStepOpened() {
        return isStepOpened(paymentStep);
    }

    public boolean isPaymentMethodSelected() {
        return !driver.findElements(selectedPaymentMethod).isEmpty();
    }

    public boolean isTermsSelected() {
        return driver.findElement(terms).isSelected();
    }

    public boolean isPlaceOrderButtonEnabled() {
        try {
            return driver.findElement(placeOrderButton).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlaceOrderButtonDisabled() {
        return !isPlaceOrderButtonEnabled();
    }

    // CHECK STATUS - XÁC NHẬN ĐƠN HÀNG
    public String getOrderConfirmationTitle() {
        try {
            String title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            orderConfirmationTitle
                    )
            ).getText().trim();

            // Loại bỏ icon ở đầu tiêu đề.
            title = title.replaceFirst(
                    "^[^\\p{L}\\p{N}]+",
                    ""
            ).trim();
            String expected = "Đơn hàng của bạn được xác nhận";

            // Chuẩn hóa chữ hoa/thường để đối chiếu với expected.
            if (title.equalsIgnoreCase(expected)) {
                return expected;
            }
            return title;
        } catch (TimeoutException e) {
            return "";
        }
    }

    // HÀM HỖ TRỢ
    private void enterText(By locator, String value) {
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
        element.clear();
        element.sendKeys(value);
    }

    private String getValidationMessage(By locator) {
        try {
            return driver.findElement(locator)
                    .getDomProperty("validationMessage");
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isElementDisplayed(By locator) {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isStepOpened(By step) {
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(step)
            );
            String className = element.getAttribute("class");
            return className != null && className.contains("-current");
        } catch (TimeoutException e) {
            return false;
        }
    }
}
