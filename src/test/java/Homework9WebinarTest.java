import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AdminPage;
import pages.CartModalPage;
import pages.LoginPage;
import pages.MainPage;
import pages.assertions.AdminAssert;
import pages.assertions.CartAssert;
import pages.assertions.MainAssert;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class Homework9WebinarTest {

    private final MainPage mainPage = new MainPage();
    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final CartModalPage cartPage = new CartModalPage();

    private final CartAssert cartAssert = new CartAssert(cartPage);
    private final AdminAssert adminAssert = new AdminAssert(adminPage);
    private final MainAssert mainAssert = new MainAssert(mainPage);

    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = ConfigProvider.getAppUrl();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = ConfigProvider.getTimeout();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("2.1. Добавить три единицы товара, оплатить (до 300 руб) и проверить уведомление")
    public void testBuyThreeItemsUnderLimit() {
        String itemName = "Pen_" + System.currentTimeMillis();
        int itemPrice = 50; // 3 * 50 = 150 руб <= 300 руб

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(itemName)
                .enterProductPrice(itemPrice)
                .clickSaveProduct()
                .goToStorefront();

        mainPage.addProductToCartByName(itemName, 3)
                .clickOpenCart();

        cartAssert.verifyModalIsOpen()
                .verifyTotalSumDoesNotExceed(300);

        cartPage.clickCheckout();

        cartAssert.verifyOrderNotificationShown();
    }

    @Test
    @DisplayName("2.2. Добавить в корзину несколько разных товаров и проверить корректность суммы")
    public void testMultipleDifferentProductsTotalSum() {
        String item1 = "Book_" + System.currentTimeMillis();
        String item2 = "Note_" + System.currentTimeMillis();
        int price1 = 70;
        int price2 = 130;
        int expectedTotal = price1 + price2;

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(item1)
                .enterProductPrice(price1)
                .clickSaveProduct();

        adminPage.enterProductName(item2)
                .enterProductPrice(price2)
                .clickSaveProduct()
                .goToStorefront();

        mainPage.addProductToCartByName(item1, 1)
                .addProductToCartByName(item2, 1)
                .clickOpenCart();

        cartAssert.verifyModalIsOpen()
                .verifyTotalSumEquals(expectedTotal);
    }

    @Test
    @DisplayName("2.3. Войти в админку, добавить товар и проверить уведомление")
    public void testAdminAddProductNotification() {
        String name = "TestItem_" + System.currentTimeMillis();
        int price = 95;

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(name)
                .enterProductPrice(price)
                .clickSaveProduct();

        adminAssert.verifyProductAddedNotification(name)
                .verifyProductInTable(name, price);
    }

    @Test
    @DisplayName("2.4. Войти в админку, отредактировать товар и проверить на витрине")
    public void testAdminEditProductAndVerifyOnStorefront() {
        String initialName = "Orig_" + System.currentTimeMillis();
        String updatedName = "Edit_" + System.currentTimeMillis();
        int updatedPrice = 110;

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(initialName)
                .enterProductPrice(80)
                .clickSaveProduct();

        adminPage.editProductInTable(initialName, updatedName, updatedPrice);

        adminPage.goToStorefront();
        mainAssert.verifyProductVisible(updatedName, updatedPrice);
    }
}