package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseTest {
    private HomePage homePage;
    private LoginPage loginPage;

    public static final String VALID_EMAIL = "khachhang1@test.com";
    public static final String WRONG_VALID_EMAIL = "1khachhang@test.com";
    public static final String INVALID_EMAIL = "khachhang1test.com";

    public static final String VALID_PASSWORD = "matkhaumanh";
    public static final String WRONG_VALID_PASSWORD = "12345678";
    public static final String INVALID_PASSWORD = "1234";

    public static final String EMPTY = "";

    @BeforeMethod
    public void openLoginPage() {
        homePage = new HomePage(driver, wait);
        loginPage = new LoginPage(driver, wait);

        homePage.openLoginPage();

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Không mở được trang Đăng nhập"
        );
    }

    private void submitForm(String email, String password) {
        loginPage.login(email, password);
    }

    private void assertBrowserValidation(
            String expectedFieldName,
            String message,
            String testCase
    ) {
        String focusedField = loginPage.getFocusedFieldName();

        System.out.println(testCase + " - Trường được focus: " + focusedField);
        System.out.println(testCase + " - Thông báo HTML5: " + message);

        Assert.assertEquals(
                focusedField,
                expectedFieldName,
                testCase + ": Trình duyệt không chặn tại trường mong đợi"
        );

        Assert.assertNotNull(
                message, testCase + ": Không lấy được thông báo HTML5"
        );

        Assert.assertFalse(
                message.trim().isEmpty(), testCase + ": Thông báo HTML5 đang rỗng"
        );
    }

    private void assertLoginError(String testCase) {
        boolean isLoginPageShown = loginPage.isLoginPageDisplayed();
        System.out.println(testCase + " - Giữ lại trang đăng nhập: " + isLoginPageShown);

        Assert.assertTrue(
                isLoginPageShown,
                testCase + ": Hệ thống không giữ lại trang đăng nhập khi thông tin không chính xác"
        );

        String pageSource = driver.getPageSource();
        boolean hasError = pageSource.contains("Người dùng lỗi") || pageSource.contains("Authentication failed");

        String errorMsg = hasError ? "Người dùng lỗi" : "Không tìm thấy thông báo";
        System.out.println(testCase + " - Thông báo lỗi trên giao diện: " + errorMsg);

        Assert.assertTrue(hasError, testCase + ": Không hiển thị thông báo lỗi trên giao diện");
    }

    private void assertLoginSuccess(String testCase) {
        boolean isLoggedIn = homePage.isLoggedIn();
        boolean isHomePage = homePage.isHomePageDisplayed();

        System.out.println(testCase + " - Trạng thái đã đăng nhập: " + isLoggedIn);
        System.out.println(testCase + " - Điều hướng về trang chủ: " + isHomePage);

        Assert.assertTrue(
                isLoggedIn,
                testCase + ": Đăng nhập thất bại, không hiển thị trạng thái đã đăng nhập"
        );

        Assert.assertTrue(
                isHomePage,
                testCase + ": Không chuyển sang trang chủ sau khi đăng nhập thành công"
        );
    }

    // TC_DN_01: Để trống hộp thư
    @Test(priority = 1)
    public void TC_DN_01() {
        submitForm(EMPTY, VALID_PASSWORD);
        assertBrowserValidation("email", loginPage.getEmailValidationMessage(), "TC_DN_01");
    }

    // TC_DN_02: Hộp thư thiếu @
    @Test(priority = 2)
    public void TC_DN_02() {
        submitForm(INVALID_EMAIL, VALID_PASSWORD);
        assertBrowserValidation("email", loginPage.getEmailValidationMessage(), "TC_DN_02");
    }

    // TC_DN_03: Sai hộp thư, để trống mật khẩu
    @Test(priority = 3)
    public void TC_DN_03() {
        submitForm(WRONG_VALID_EMAIL, EMPTY);
        assertBrowserValidation("password", loginPage.getPasswordValidationMessage(), "TC_DN_03");
    }

    // TC_DN_04: Sai hộp thư, mật khẩu sai định dạng
    @Test(priority = 4)
    public void TC_DN_04() {
        submitForm(WRONG_VALID_EMAIL, INVALID_PASSWORD);
        assertBrowserValidation("password", loginPage.getPasswordValidationMessage(), "TC_DN_04");
    }

    // TC_DN_05: Sai hộp thư, sai mật khẩu
    @Test(priority = 5)
    public void TC_DN_05() {
        submitForm(WRONG_VALID_EMAIL, WRONG_VALID_PASSWORD);
        assertLoginError("TC_DN_05");
    }

    // TC_DN_06: Sai hộp thư, đúng mật khẩu
    @Test(priority = 6)
    public void TC_DN_06() {
        submitForm(WRONG_VALID_EMAIL, VALID_PASSWORD);
        assertLoginError("TC_DN_06");
    }

    // TC_DN_07: Đúng hộp thư, để trống mật khẩu
    @Test(priority = 7)
    public void TC_DN_07() {
        submitForm(VALID_EMAIL, EMPTY);
        assertBrowserValidation("password", loginPage.getPasswordValidationMessage(), "TC_DN_07");
    }

    // TC_DN_08: Đúng hộp thư, mật khẩu sai định dạng
    @Test(priority = 8)
    public void TC_DN_08() {
        submitForm(VALID_EMAIL, INVALID_PASSWORD);
        assertBrowserValidation("password", loginPage.getPasswordValidationMessage(), "TC_DN_08");
    }

    // TC_DN_09: Đúng hộp thư, sai mật khẩu
    @Test(priority = 9)
    public void TC_DN_09() {
        submitForm(VALID_EMAIL, WRONG_VALID_PASSWORD);
        assertLoginError("TC_DN_09");
    }

    // TC_DN_10: Đúng hộp thư, đúng mật khẩu
    @Test(priority = 10)
    public void TC_DN_10() {
        submitForm(VALID_EMAIL, VALID_PASSWORD);
        assertLoginSuccess("TC_DN_10");
    }
}
