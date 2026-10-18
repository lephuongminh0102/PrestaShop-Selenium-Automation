package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class RegisterPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // LOCATORS
    private By registerForm = By.id("customer-form");
    private By firstName = By.id("field-firstname");
    private By lastName = By.id("field-lastname");
    private By email = By.id("field-email");
    private By password = By.id("field-password");

    private By showPasswordButton =
            By.cssSelector("button[data-action='show-password']");

    private By terms =
            By.cssSelector("#customer-form input[name='psgdpr']");

    private By customerPrivacy =
            By.cssSelector("#customer-form input[name='customer_privacy']");

    private By saveButton =
            By.cssSelector("#customer-form button[data-link-action='save-customer']");

    // LOCATORS LỖI
    private By firstNameErrors = createErrorLocator("field-firstname");
    private By lastNameErrors = createErrorLocator("field-lastname");
    private By emailErrors = createErrorLocator("field-email");
    private By passwordErrors = createErrorLocator("field-password");

    private By createErrorLocator(String fieldId) {
        return By.xpath(
                "//input[@id='" + fieldId + "']" +
                        "/ancestor::div[contains(@class,'js-input-column')][1]" +
                        "//div[contains(@class,'help-block')]//li[contains(@class,'alert-danger')]"
        );
    }

    // CONSTRUCTOR
    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ACTIONS
    public void enterFirstName(String value) {
        enterText(firstName, value);
    }

    public void enterLastName(String value) {
        enterText(lastName, value);
    }

    public void enterEmail(String value) {
        enterText(email, value);
    }

    public void enterPassword(String value) {
        enterText(password, value);
    }

    private void enterText(By locator, String value) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
        input.clear();
        input.sendKeys(value);
    }

    public void clickShowPassword() {
        wait.until(
                ExpectedConditions.elementToBeClickable(showPasswordButton)
        ).click();
    }

    public void selectTerms() {
        selectCheckbox(terms);
    }

    public void selectCustomerPrivacy() {
        selectCheckbox(customerPrivacy);
    }

    private void selectCheckbox(By locator) {
        WebElement checkbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickSave() {
        wait.until(
                ExpectedConditions.elementToBeClickable(saveButton)
        ).click();
    }

    // CHECK STATUS
    public boolean isRegisterPageDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(registerForm)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // VALIDATION HTML5
    public String getFirstNameValidationMessage() {
        return getValidationMessage(firstName);
    }

    public String getLastNameValidationMessage() {
        return getValidationMessage(lastName);
    }

    public String getEmailValidationMessage() {
        return getValidationMessage(email);
    }

    public String getPasswordValidationMessage() {
        return getValidationMessage(password);
    }

    public String getTermsValidationMessage() {
        return getValidationMessage(terms);
    }

    public String getCustomerPrivacy() {
        return getValidationMessage(customerPrivacy);
    }

    private String getValidationMessage(By locator) {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        ).getDomProperty("validationMessage");
    }

    // LỖI INLINE
    public String getFirstNameError() {
        return getError(firstNameErrors);
    }

    public String getLastNameError() {
        return getError(lastNameErrors);
    }

    public String getEmailError() {
        return getError(emailErrors);
    }

    public String getPasswordError() {
        return getError(passwordErrors);
    }

    private String getError(By locator) {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            ).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
}