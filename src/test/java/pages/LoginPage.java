package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // LOCATORS
    private By email = By.id("field-email");
    private By password = By.id("field-password");
    private By loginButton = By.id("submit-login");

    private By showPasswordButton =
            By.cssSelector("button[data-action='show-password']");

    private By registerLink =
            By.cssSelector("a[data-link-action='display-register-form']");

    // CONSTRUCTOR
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ACTIONS
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

    public void clickLogin() {
        wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        ).click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public void openRegisterPage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(registerLink)
        ).click();
    }

    // VALIDATION HTML5
    public String getEmailValidationMessage() {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(email)
        ).getDomProperty("validationMessage");
    }

    public String getPasswordValidationMessage() {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(password)
        ).getDomProperty("validationMessage");
    }

    public String getFocusedFieldName() {
        return driver.switchTo()
                .activeElement()
                .getDomAttribute("name");
    }

    // CHECK STATUS
    public boolean isLoginPageDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(loginButton)
            ).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}