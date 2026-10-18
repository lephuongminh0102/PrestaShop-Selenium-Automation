package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;

public class AddToCartTest extends BaseTest {
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    private static final String OUT_OF_STOCK_PRODUCT = "Xiaomi Redmi Note 13";
    private static final String IN_STOCK_PRODUCT = "Samsung Galaxy S24";

    private static final String INVALID_QUANTITY = "-1";
    private static final String EXCEED_STOCK_QUANTITY = "901";
    private static final String VALID_QUANTITY = "2";

    @BeforeMethod
    public void createPages() {
        homePage = new HomePage(driver, wait);
        productPage = new ProductPage(driver, wait);
        cartPage = new CartPage(driver, wait);
    }

    private void assertOutOfStock(String testCase) {
        String availabilityMsg = productPage.getAvailabilityMessage();
        boolean isOutOfStock = availabilityMsg.contains("Hết hàng") || availabilityMsg.contains("Out of stock");

        System.out.println(testCase + " - Thông báo trạng thái kho: " + availabilityMsg);

        Assert.assertTrue(
                isOutOfStock,
                testCase + ": Sản phẩm không hiển thị trạng thái hết hàng"
        );

        boolean isButtonEnabled = productPage.isAddToCartButtonEnabled();
        System.out.println(testCase + " - Nút thêm vào giỏ hàng khả dụng: " + isButtonEnabled);

        Assert.assertFalse(
                isButtonEnabled,
                testCase + ": Nút thêm vào giỏ hàng vẫn khả dụng đối với sản phẩm hết hàng"
        );
    }

    private void assertInvalidQuantity(String testCase) {
        boolean isPopupShown = productPage.isAddToCartPopupDisplayed();
        System.out.println(testCase + " - Hiển thị popup giỏ hàng (Số lượng không hợp lệ): " + isPopupShown);

        // Khi nhập số lượng không hợp lệ (số âm -1), hệ thống phải CHẶN và KHÔNG ĐƯỢC HIỆN POPUP
        Assert.assertFalse(
                isPopupShown,
                testCase + ": Hệ thống chấp nhận số lượng không hợp lệ và hiển thị giỏ hàng"
        );
    }

    private void assertExceedStock(String testCase) {
        boolean hasStockWarning = false;
        try {
            // Sử dụng WebDriverWait để chờ tối đa 5 giây cho đến khi thông báo xuất hiện trong DOM
            org.openqa.selenium.support.ui.WebDriverWait shortWait =
                    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));

            shortWait.until(d -> {
                String pageSource = d.getPageSource();
                return pageSource.contains("Không có đủ sản phẩm trong kho")
                        || pageSource.contains("Not enough products in stock");
            });
            hasStockWarning = true;
        } catch (Exception e) {
            hasStockWarning = false;
        }

        String displayMsg = hasStockWarning ? "Không có đủ sản phẩm trong kho" : "Không tìm thấy thông báo";
        System.out.println(testCase + " - Thông báo từ hệ thống: " + displayMsg);

        Assert.assertTrue(
                hasStockWarning,
                testCase + ": Không hiển thị cảnh báo 'Không có đủ sản phẩm trong kho'"
        );

        boolean isButtonEnabled = productPage.isAddToCartButtonEnabled();
        System.out.println(testCase + " - Nút thêm vào giỏ hàng khả dụng: " + isButtonEnabled);

        Assert.assertFalse(
                isButtonEnabled,
                testCase + ": Nút thêm vào giỏ hàng vẫn khả dụng đối với sản phẩm hết hàng"
        );
    }

    private void assertAddToCartSuccess(String testCase) {
        boolean isPopupDisplayed = productPage.isAddToCartPopupDisplayed();
        System.out.println(testCase + " - Hiển thị popup xác nhận thêm vào giỏ: " + isPopupDisplayed);

        Assert.assertTrue(
                isPopupDisplayed,
                testCase + ": Không hiển thị popup xác nhận thêm sản phẩm vào giỏ hàng"
        );
    }

    // TC_GH_01: Kiểm tra sản phẩm hết hàng
    @Test(priority = 1)
    public void TC_GH_01() {
        homePage.searchProduct(OUT_OF_STOCK_PRODUCT);
        homePage.openProductByImage();
        assertOutOfStock("TC_GH_01");
    }

    // TC_GH_02: Nhập số lượng không hợp lệ (số âm)
    @Test(priority = 2)
    public void TC_GH_02() {
        homePage.searchProduct(IN_STOCK_PRODUCT);
        homePage.openProductByImage();
        productPage.setQuantity(INVALID_QUANTITY);
        productPage.clickAddToCart();
        assertInvalidQuantity("TC_GH_02");
    }

    // TC_GH_03: Nhập số lượng vượt quá giới hạn kho
    @Test(priority = 3)
    public void TC_GH_03() {
        homePage.searchProduct(IN_STOCK_PRODUCT);
        homePage.openProductByImage();
        productPage.setQuantity(EXCEED_STOCK_QUANTITY);
        productPage.clickAddToCart();
        assertExceedStock("TC_GH_03");
    }

    // TC_GH_04: Thêm sản phẩm vào giỏ hàng với số lượng hợp lệ
    @Test(priority = 4)
    public void TC_GH_04() {
        homePage.searchProduct(IN_STOCK_PRODUCT);
        homePage.openProductByImage();
        productPage.setQuantity(VALID_QUANTITY);
        productPage.clickAddToCart();
        assertAddToCartSuccess("TC_GH_04");
    }
}
