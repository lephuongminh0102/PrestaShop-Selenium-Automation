
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import pages.ProductPage;
import pages.CartPage;
import pages.CheckoutPage;

public class CheckoutTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    // DỮ LIỆU KIỂM THỬ

    private static final String CUSTOMER_FIRST_NAME = "Minh";
    private static final String CUSTOMER_LAST_NAME = "Le";
    private static final String CUSTOMER_PASSWORD = "Minh123#abc";
    private static final String CITY = "Hà Nội";
    private static final String ADDRESS = "54 Triều Khúc";
    private static final String PHONE = "0123456789";
    private static final String INVALID_PHONE = "1234";
    private static final String EMPTY = "";

    private static final String MSG_REQUIRED = "Vui lòng điền vào trường này.";
    private static final String MSG_INVALID_FORMAT = "Định dạng không hợp lệ.";
    private static final String MSG_ORDER_CONFIRMED = "Đơn hàng của bạn được xác nhận";

    @BeforeMethod
    public void createPages() {
        homePage = new HomePage(driver, wait);
        loginPage = new LoginPage(driver, wait);
        registerPage = new RegisterPage(driver, wait);
        productPage = new ProductPage(driver, wait);
        cartPage = new CartPage(driver, wait);
        checkoutPage = new CheckoutPage(driver, wait);
    }

    private String extractMainMessage(String actual) {
        return actual == null ? "" : actual.trim();
    }

    private void finishTestCase(String testCase, String expected, String actual) {
        String actualMain = extractMainMessage(actual);
        boolean passed = actualMain.equals(expected);
        System.out.println(
                "\n[" + testCase + "] " + (passed ? "PASS" : "FAIL")
        );
        System.out.println("   Kết quả mong đợi: " + expected);
        System.out.println("   Kết quả thực tế : " + actualMain);
        Assert.assertTrue(
                passed,
                testCase + " thất bại. Kết quả thực tế: " + actualMain
        );
    }

    private String newEmail() {
        return "checkout" + System.currentTimeMillis() + "@example.com";
    }

    private boolean containsIgnoreCase(String actual, String expected) {
        return actual != null
                && actual.toLowerCase().contains(expected.toLowerCase());
    }

    private void registerNewCustomer() {
        String email = newEmail();
        homePage.openLoginPage();
        loginPage.openRegisterPage();
        Assert.assertTrue(
                registerPage.isRegisterPageDisplayed(),
                "Không mở được trang đăng ký"
        );
        registerPage.enterFirstName(CUSTOMER_FIRST_NAME);
        registerPage.enterLastName(CUSTOMER_LAST_NAME);
        registerPage.enterEmail(email);
        registerPage.enterPassword(CUSTOMER_PASSWORD);
        registerPage.selectTerms();
        registerPage.selectCustomerPrivacy();
        registerPage.clickSave();
        Assert.assertTrue(
                homePage.isLoggedIn(),
                "Đăng ký xong nhưng tài khoản chưa đăng nhập"
        );
    }

    private void addProductAndOpenCart() {
        homePage.openProductByImage();
        Assert.assertTrue(
                productPage.isProductPageDisplayed(),
                "Không mở được trang sản phẩm"
        );
        productPage.clickAddToCart();
        Assert.assertTrue(
                productPage.isAddToCartPopupDisplayed(),
                "Popup thêm sản phẩm vào giỏ hàng không hiển thị"
        );
        productPage.clickCheckoutLink();
        Assert.assertTrue(
                cartPage.hasProduct(),
                "Giỏ hàng chưa có sản phẩm"
        );
    }

    private void clickCheckoutAndCheckAddressForm() {
        cartPage.clickCheckoutLink();
        Assert.assertTrue(
                checkoutPage.isAddressStepOpened(),
                "Bước địa chỉ chưa được mở"
        );
        Assert.assertTrue(
                checkoutPage.isAddressFormDisplayed(),
                "Không hiển thị đầy đủ các trường địa chỉ"
        );
    }

    private void goToPaymentStep() {
        checkoutPage.enterCity(CITY);
        checkoutPage.enterAddress(ADDRESS);
        checkoutPage.enterPhone(PHONE);
        checkoutPage.clickContinueAddress();
        Assert.assertTrue(
                checkoutPage.isShippingStepOpened(),
                "Không chuyển được sang bước vận chuyển"
        );
        checkoutPage.selectShippingMethod();
        Assert.assertTrue(
                checkoutPage.isShippingMethodSelected(),
                "Chưa chọn được phương thức vận chuyển"
        );
        checkoutPage.clickContinueShipping();
        Assert.assertTrue(
                checkoutPage.isPaymentStepOpened(),
                "Không chuyển được sang bước thanh toán"
        );
    }

    // TC_TT_01: Chưa đăng nhập
    @Test(priority = 1)
    public void TC_TT_01() {
        addProductAndOpenCart();
        cartPage.clickCheckoutLink();
        String expected = "Bước Thông tin cá nhân đang mở";
        String actual = checkoutPage.isPersonalInformationStepOpened()
                ? "Bước Thông tin cá nhân đang mở"
                : "Bước Thông tin cá nhân chưa mở";
        finishTestCase("TC_TT_01", expected, actual);
    }

    @Test(priority = 2)
    public void TC_TT_02() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();

        checkoutPage.enterCity(EMPTY);
        checkoutPage.enterAddress(ADDRESS);
        checkoutPage.enterPhone(PHONE);
        checkoutPage.clickContinueAddress();

        String expected = MSG_REQUIRED;
        String actual = checkoutPage.getCityValidationMessage();
        finishTestCase("TC_TT_02", expected, actual);
    }

    @Test(priority = 3)
    public void TC_TT_03() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();

        checkoutPage.enterCity(CITY);
        checkoutPage.enterAddress(EMPTY);
        checkoutPage.enterPhone(PHONE);
        checkoutPage.clickContinueAddress();

        String expected = MSG_REQUIRED;
        String actual = checkoutPage.getAddressValidationMessage();
        finishTestCase("TC_TT_03", expected, actual);
    }

    @Test(priority = 4)
    public void TC_TT_04() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();

        checkoutPage.enterCity(CITY);
        checkoutPage.enterAddress(ADDRESS);
        checkoutPage.enterPhone(EMPTY);
        checkoutPage.clickContinueAddress();

        String expected = MSG_REQUIRED;
        String actual = checkoutPage.getPhoneValidationMessage();
        finishTestCase("TC_TT_04", expected, actual);
    }

    @Test(priority = 5)
    public void TC_TT_05() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();

        checkoutPage.enterCity(CITY);
        checkoutPage.enterAddress(ADDRESS);
        checkoutPage.enterPhone(INVALID_PHONE);
        checkoutPage.clickContinueAddress();

        String expected = MSG_INVALID_FORMAT;
        String actual = checkoutPage.getPhoneErrors();
        if (actual.isEmpty()) {
            if (checkoutPage.isShippingStepOpened()) {
                actual = "Hệ thống vẫn chuyển sang bước vận chuyển "
                        + "với số điện thoại " + INVALID_PHONE;
            } else {
                actual = "Không hiển thị lỗi định dạng số điện thoại";
            }
        }
        finishTestCase("TC_TT_05", expected, actual);
    }

    @Test(priority = 6)
    public void TC_TT_06() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();
        goToPaymentStep();
        boolean noPaymentSelected = !checkoutPage.isPaymentMethodSelected();
        boolean buttonDisabled = checkoutPage.isPlaceOrderButtonDisabled();
        String expected =
                "Chưa chọn phương thức thanh toán; "
                        + "Nút Đặt hàng bị vô hiệu hóa";

        String actual =
                (noPaymentSelected
                        ? "Chưa chọn phương thức thanh toán"
                        : "Đã chọn phương thức thanh toán")
                        + "; "
                        + (buttonDisabled
                        ? "Nút Đặt hàng bị vô hiệu hóa"
                        : "Nút Đặt hàng đang được bật");

        finishTestCase("TC_TT_06", expected, actual);
    }

    @Test(priority = 7)
    public void TC_TT_07() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();
        goToPaymentStep();

        checkoutPage.selectCashOnDelivery();

        boolean paymentSelected =
                checkoutPage.isPaymentMethodSelected();

        boolean termsUnchecked =
                !checkoutPage.isTermsSelected();

        boolean buttonDisabled =
                checkoutPage.isPlaceOrderButtonDisabled();

        String expected =
                "Đã chọn phương thức thanh toán; "
                        + "Chưa tích điều khoản; "
                        + "Nút Đặt hàng bị vô hiệu hóa";

        String actual =
                (paymentSelected
                        ? "Đã chọn phương thức thanh toán"
                        : "Chưa chọn phương thức thanh toán")
                        + "; "
                        + (termsUnchecked
                        ? "Chưa tích điều khoản"
                        : "Đã tích điều khoản")
                        + "; "
                        + (buttonDisabled
                        ? "Nút Đặt hàng bị vô hiệu hóa"
                        : "Nút Đặt hàng đang được bật");

        finishTestCase("TC_TT_07", expected, actual);
    }

    @Test(priority = 8)
    public void TC_TT_08() {
        registerNewCustomer();
        addProductAndOpenCart();
        clickCheckoutAndCheckAddressForm();
        goToPaymentStep();

        checkoutPage.selectCashOnDelivery();
        checkoutPage.selectTerms();

        boolean paymentSelected =
                checkoutPage.isPaymentMethodSelected();

        boolean termsSelected =
                checkoutPage.isTermsSelected();

        boolean buttonEnabled =
                checkoutPage.isPlaceOrderButtonEnabled();

        String expected = MSG_ORDER_CONFIRMED;
        String actual;

        if (paymentSelected && termsSelected && buttonEnabled) {
            checkoutPage.clickPlaceOrder();
            actual = checkoutPage.getOrderConfirmationTitle();
        } else {
            actual = "Chưa đủ điều kiện đặt hàng. "
                    + "Đã chọn thanh toán: " + paymentSelected
                    + "; Đã tích điều khoản: " + termsSelected
                    + "; Nút Đặt hàng được bật: " + buttonEnabled;
        }

        finishTestCase("TC_TT_08", expected, actual);
    }

}
