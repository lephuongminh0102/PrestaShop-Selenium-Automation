package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import org.testng.asserts.SoftAssert;

public class RegisterTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    private static final String VALID_FIRST_NAME = "Minh";
    private static final String VALID_LAST_NAME = "Le";
    private static final String INVALID_FIRST_NAME = "123";
    private static final String INVALID_LAST_NAME = "#$";
    private static final String DUPLICATE_EMAIL = "khachhang1@test.com";
    private static final String VALID_PASSWORD = "Minh123#abc";
    private static final String SHORT_PASSWORD = "Minh123";
    private static final String EMPTY = "";

    @BeforeMethod
    public void openRegisterPage() {
        homePage = new HomePage(driver, wait);
        loginPage = new LoginPage(driver, wait);
        registerPage = new RegisterPage(driver, wait);
        homePage.openLoginPage();
        loginPage.openRegisterPage();
        Assert.assertTrue(
                registerPage.isRegisterPageDisplayed(),
                "Không mở được trang Đăng ký"
        );
    }

    private String newEmail() {
        return "minh" + System.currentTimeMillis() + "@gmail.com";
    }

    private void submitForm(String firstName, String lastName, String email, String password) throws InterruptedException{
        registerPage.enterFirstName(firstName); //Thread.sleep(500);
        registerPage.enterLastName(lastName); //Thread.sleep(500);
        registerPage.enterEmail(email); //Thread.sleep(500);
        registerPage.clickShowPassword();
        registerPage.enterPassword(password); //Thread.sleep(500);
        registerPage.selectTerms();
        registerPage.selectCustomerPrivacy(); //Thread.sleep(500);
        registerPage.clickSave(); //Thread.sleep(2000);
    }

    private String extractMainMessage(String message) {
        if (message == null) {
            return null;
        }
        if (message.contains("Vui lòng bao gồm '@' trong địa chỉ email.")) {
            return "Vui lòng bao gồm '@' trong địa chỉ email.";
        }
        if (message.contains("Vui lòng nhập phần đứng sau '@'.")) {
            return "Vui lòng nhập phần đứng sau '@'.";
        }
        if (message.contains("Mật khẩu phải có độ dài từ 8 đến 72 ký tự.")) {
            return "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        }
        return message;
    }

    private void assertInlineError(String actual, String expected, String fieldName, String testCase, SoftAssert softAssert) {
        boolean passed = actual != null && extractMainMessage(actual).equals(expected);
        softAssert.assertTrue(
                passed,
                testCase + " - Trường " + fieldName
                        + "\nKết quả mong đợi: " + expected
                        + "\nKết quả thực tế: " + actual
        );
    }

    private void finishTestCase(String testCase, SoftAssert softAssert, String expected, String actual) {
        AssertionError failure = null;
        try {
            softAssert.assertAll();
        } catch (AssertionError e) {
            failure = e;
        }
        System.out.println("[" + testCase + "] "
                + (failure == null ? "PASS" : "FAIL"));
        System.out.println("Kết quả mong đợi: " + expected);
        System.out.println("Kết quả thực tế: " + actual);
        if (failure != null) {
            throw failure;
        }
    }

    // TC_DK_01: Tên để trống
    @Test(priority = 1)
    public void TC_DK_01() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(EMPTY, VALID_LAST_NAME, newEmail(), VALID_PASSWORD);

        String actual = registerPage.getFirstNameValidationMessage();
        String expected = "Vui lòng điền vào trường này.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_01", softAssert, expected, actual);
    }

    // TC_DK_02: Họ để trống
    @Test(priority = 2)
    public void TC_DK_02() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, EMPTY, newEmail(), VALID_PASSWORD);

        String actual = registerPage.getLastNameValidationMessage();
        String expected = "Vui lòng điền vào trường này.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_02", softAssert, expected, actual);
    }

    // TC_DK_03: Email để trống
    @Test(priority = 3)
    public void TC_DK_03() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, EMPTY, VALID_PASSWORD);

        String actual = registerPage.getEmailValidationMessage();
        String expected = "Vui lòng điền vào trường này.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_03", softAssert, expected, actual);
    }

    // TC_DK_04: Mật khẩu để trống
    @Test(priority = 4)
    public void TC_DK_04() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, newEmail(), EMPTY);

        String actual = registerPage.getPasswordValidationMessage();
        String expected = "Vui lòng điền vào trường này.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_04", softAssert, expected, actual);
    }

    // TC_DK_05: Không tích điều khoản
    @Test(priority = 5)
    public void TC_DK_05() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        registerPage.enterFirstName(VALID_FIRST_NAME); Thread.sleep(500);
        registerPage.enterLastName(VALID_LAST_NAME); Thread.sleep(500);
        registerPage.enterEmail(newEmail()); Thread.sleep(500);
        registerPage.clickShowPassword();
        registerPage.enterPassword(VALID_PASSWORD); Thread.sleep(500);
        registerPage.clickSave(); Thread.sleep(2000);

        String actual = registerPage.getTermsValidationMessage();
        String expected = "Vui lòng chọn hộp kiểm này nếu bạn muốn tiếp tục.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_05", softAssert, expected, actual);
    }

    // TC_DK_06: Email thiếu @
    @Test(priority = 6)
    public void TC_DK_06() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, "abcgmail.com", VALID_PASSWORD);

        String actual = extractMainMessage(
                registerPage.getEmailValidationMessage()
        );
        String expected = "Vui lòng bao gồm '@' trong địa chỉ email.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_06", softAssert, expected, actual);
    }

    // TC_DK_07: Email thiếu phần sau @
    @Test(priority = 7)
    public void TC_DK_07() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, "abc@", VALID_PASSWORD);

        String actual = extractMainMessage(
                registerPage.getEmailValidationMessage()
        );
        String expected = "Vui lòng nhập phần đứng sau '@'.";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_07", softAssert, expected, actual);
    }

    // TC_DK_08: Đăng ký hợp lệ
    @Test(priority = 8)
    public void TC_DK_08() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, newEmail(), VALID_PASSWORD);

        boolean homePageDisplayed = homePage.isHomePageDisplayed();
        String expected = "Đăng ký tài khoản thành công, quay lại trang chủ";
        String actual = homePageDisplayed
                ? "Đăng ký tài khoản thành công, quay lại trang chủ"
                : "Đăng ký tài khoản thất bại";
        softAssert.assertEquals(actual, expected);
        finishTestCase("TC_DK_08", softAssert, expected, actual);
    }

    // TC_DK_09: Mật khẩu ngắn
    @Test(priority = 9)
    public void TC_DK_09() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, newEmail(), SHORT_PASSWORD);

        String actual = extractMainMessage(registerPage.getPasswordError());
        String expected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(actual, expected, "Mật khẩu", "TC_DK_09", softAssert);
        finishTestCase("TC_DK_09", softAssert, expected, actual);
    }

    // TC_DK_10: Email trùng
    @Test(priority = 10)
    public void TC_DK_10() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, DUPLICATE_EMAIL, VALID_PASSWORD);

        String actual = registerPage.getEmailError();
        String expected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(actual, expected, "Email", "TC_DK_10", softAssert);
        finishTestCase("TC_DK_10", softAssert, expected, actual);
    }

    // TC_DK_11: Email trùng + mật khẩu ngắn
    @Test(priority = 11)
    public void TC_DK_11() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, VALID_LAST_NAME, DUPLICATE_EMAIL, SHORT_PASSWORD);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_11", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_11", softAssert);

        String expected = "\n\tEmail: " + emailExpected + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tEmail: " + emailActual + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_11", softAssert, expected, actual);
    }

    // TC_DK_12: Họ sai định dạng
    @Test(priority = 12)
    public void TC_DK_12() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, INVALID_LAST_NAME, newEmail(), VALID_PASSWORD);

        String actual = registerPage.getLastNameError();
        String expected = "Định dạng không hợp lệ.";
        assertInlineError(actual, expected, "Họ", "TC_DK_12", softAssert);
        finishTestCase("TC_DK_12", softAssert, expected, actual);
    }

    // TC_DK_13: Họ sai + mật khẩu ngắn
    @Test(priority = 13)
    public void TC_DK_13() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, INVALID_LAST_NAME, newEmail(), SHORT_PASSWORD);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_13", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_13", softAssert);

        String expected = "\n\tHọ: " + lastNameExpected + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tHọ: " + lastNameActual + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_13", softAssert, expected, actual);
    }

    // TC_DK_14: Họ sai + email trùng
    @Test(priority = 14)
    public void TC_DK_14() throws InterruptedException{
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, INVALID_LAST_NAME, DUPLICATE_EMAIL, VALID_PASSWORD);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_14", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_14", softAssert);

        String expected = "\n\tHọ: " + lastNameExpected + "\n\tEmail: " + emailExpected;
        String actual = "\n\tHọ: " + lastNameActual + "\n\tEmail: " + emailActual;
        finishTestCase("TC_DK_14", softAssert, expected, actual);
    }

    // TC_DK_15: Họ sai + email trùng + mật khẩu ngắn
    @Test(priority = 15)
    public void TC_DK_15() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(VALID_FIRST_NAME, INVALID_LAST_NAME, DUPLICATE_EMAIL, SHORT_PASSWORD);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_15", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_15", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_15", softAssert);

        String expected = "\n\tHọ: " + lastNameExpected
                + "\n\tEmail: " + emailExpected
                + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tHọ: " + lastNameActual
                + "\n\tEmail: " + emailActual
                + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_15", softAssert, expected, actual);
    }

    // TC_DK_16: Tên sai định dạng
    @Test(priority = 16)
    public void TC_DK_16() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, VALID_LAST_NAME, newEmail(), VALID_PASSWORD);

        String actual = registerPage.getFirstNameError();
        String expected = "Định dạng không hợp lệ.";
        assertInlineError(actual, expected, "Tên", "TC_DK_16", softAssert);
        finishTestCase("TC_DK_16", softAssert, expected, actual);
    }

    // TC_DK_17: Tên sai + mật khẩu ngắn
    @Test(priority = 17)
    public void TC_DK_17() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, VALID_LAST_NAME, newEmail(), SHORT_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_17", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_17", softAssert);

        String expected = "\n\tTên: " + firstNameExpected + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tTên: " + firstNameActual + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_17", softAssert, expected, actual);
    }

    // TC_DK_18: Tên sai + email trùng
    @Test(priority = 18)
    public void TC_DK_18() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, VALID_LAST_NAME, DUPLICATE_EMAIL, VALID_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_18", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_18", softAssert);

        String expected = "\n\tTên: " + firstNameExpected + "\n\tEmail: " + emailExpected;
        String actual = "\n\tTên: " + firstNameActual + "\n\tEmail: " + emailActual;
        finishTestCase("TC_DK_18", softAssert, expected, actual);
    }

    // TC_DK_19: Tên sai + email trùng + mật khẩu ngắn
    @Test(priority = 19)
    public void TC_DK_19() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, VALID_LAST_NAME, DUPLICATE_EMAIL, SHORT_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_19", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_19", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_19", softAssert);

        String expected = "\n\tTên: " + firstNameExpected
                + "\n\tEmail: " + emailExpected
                + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tTên: " + firstNameActual
                + "\n\tEmail: " + emailActual
                + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_19", softAssert, expected, actual);
    }

    // TC_DK_20: Tên + họ sai
    @Test(priority = 20)
    public void TC_DK_20() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, INVALID_LAST_NAME, newEmail(), VALID_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_20", softAssert);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_20", softAssert);

        String expected = "\n\tTên: " + firstNameExpected + "\n\tHọ: " + lastNameExpected;
        String actual = "\n\tTên: " + firstNameActual + "\n\tHọ: " + lastNameActual;
        finishTestCase("TC_DK_20", softAssert, expected, actual);
    }

    // TC_DK_21: Tên + họ sai + mật khẩu ngắn
    @Test(priority = 21)
    public void TC_DK_21() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, INVALID_LAST_NAME, newEmail(), SHORT_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_21", softAssert);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_21", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_21", softAssert);

        String expected = "\n\tTên: " + firstNameExpected
                + "\n\tHọ: " + lastNameExpected
                + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tTên: " + firstNameActual
                + "\n\tHọ: " + lastNameActual
                + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_21", softAssert, expected, actual);
    }

    // TC_DK_22: Tên + họ sai + email trùng
    @Test(priority = 22)
    public void TC_DK_22() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, INVALID_LAST_NAME, DUPLICATE_EMAIL, VALID_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_22", softAssert);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_22", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_22", softAssert);

        String expected = "\n\tTên: " + firstNameExpected
                + "\n\tHọ: " + lastNameExpected
                + "\n\tEmail: " + emailExpected;
        String actual = "\n\tTên: " + firstNameActual
                + "\n\tHọ: " + lastNameActual
                + "\n\tEmail: " + emailActual;
        finishTestCase("TC_DK_22", softAssert, expected, actual);
    }

    // TC_DK_23: Tên + họ sai + email trùng + mật khẩu ngắn
    @Test(priority = 23)
    public void TC_DK_23() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        submitForm(INVALID_FIRST_NAME, INVALID_LAST_NAME, DUPLICATE_EMAIL, SHORT_PASSWORD);

        String firstNameActual = registerPage.getFirstNameError();
        String firstNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(firstNameActual, firstNameExpected, "Tên", "TC_DK_23", softAssert);

        String lastNameActual = registerPage.getLastNameError();
        String lastNameExpected = "Định dạng không hợp lệ.";
        assertInlineError(lastNameActual, lastNameExpected, "Họ", "TC_DK_23", softAssert);

        String emailActual = registerPage.getEmailError();
        String emailExpected = "Email này đã được sử dụng, vui lòng chọn email khác hoặc đăng nhập.";
        assertInlineError(emailActual, emailExpected, "Email", "TC_DK_23", softAssert);

        String passwordActual = extractMainMessage(registerPage.getPasswordError());
        String passwordExpected = "Mật khẩu phải có độ dài từ 8 đến 72 ký tự.";
        assertInlineError(passwordActual, passwordExpected, "Mật khẩu", "TC_DK_23", softAssert);

        String expected = "\n\tTên: " + firstNameExpected
                + "\n\tHọ: " + lastNameExpected
                + "\n\tEmail: " + emailExpected
                + "\n\tMật khẩu: " + passwordExpected;
        String actual = "\n\tTên: " + firstNameActual
                + "\n\tHọ: " + lastNameActual
                + "\n\tEmail: " + emailActual
                + "\n\tMật khẩu: " + passwordActual;
        finishTestCase("TC_DK_23", softAssert, expected, actual);
    }

}