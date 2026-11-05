package tests;

import assertions.AdminAssert;
import assertions.CartAssert;
import assertions.MainAssert;
import config.ConfigProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdminPage;
import pages.CartModalPage;
import pages.LoginPage;
import pages.MainPage;

@Tag("ui")
public class StoreUiTest extends BaseUiTest {

    private final MainPage mainPage = new MainPage();
    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final CartModalPage cartPage = new CartModalPage();

    private final CartAssert cartAssert = new CartAssert(cartPage);
    private final AdminAssert adminAssert = new AdminAssert(adminPage);
    private final MainAssert mainAssert = new MainAssert(mainPage);

    @Test
    @Tag("smoke")
    @DisplayName("2.1. Добавить 3 единицы товара до 300 руб, оформить и проверить уведомление (Smoke)")
    public void testBuyThreeItemsUnderLimit() {
        String itemName = "Pen_" + System.currentTimeMillis();
        int itemPrice = 50;

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
    @DisplayName("2.2. Несколько разных товаров и проверка общей суммы")
    public void testMultipleDifferentProductsTotalSum() {
        String item1 = "Book_" + System.currentTimeMillis();
        String item2 = "Note_" + System.currentTimeMillis();
        int price1 = 70;
        int price2 = 130;
        int expectedTotal = price1 + price2;

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(item1).enterProductPrice(price1).clickSaveProduct();
        adminPage.enterProductName(item2).enterProductPrice(price2).clickSaveProduct().goToStorefront();

        mainPage.addProductToCartByName(item1, 1)
                .addProductToCartByName(item2, 1)
                .clickOpenCart();

        cartAssert.verifyModalIsOpen().verifyTotalSumEquals(expectedTotal);
    }

    @Test
    @Tag("smoke")
    @DisplayName("2.3. Создание товара через админку и проверка в таблице (Smoke)")
    public void testAdminAddProductNotification() {
        String name = ConfigProvider.getProductName() + "_" + System.currentTimeMillis();
        int price = ConfigProvider.getProductPrice();

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(name)
                .enterProductPrice(price)
                .clickSaveProduct();

        adminAssert.verifyProductAddedNotification(name)
                .verifyProductInTable(name, price);
    }

    @Test
    @DisplayName("2.4. Редактирование товара в админке и проверка на витрине")
    public void testAdminEditProductAndVerifyOnStorefront() {
        String initialName = "Orig_" + System.currentTimeMillis();
        String updatedName = "Edit_" + System.currentTimeMillis();
        int updatedPrice = 110;

        loginPage.openPage()
                .login(ConfigProvider.getAdminUsername(), ConfigProvider.getAdminPassword());

        adminPage.enterProductName(initialName).enterProductPrice(80).clickSaveProduct();
        adminPage.editProductInTable(initialName, updatedName, updatedPrice);
        adminPage.goToStorefront();

        mainAssert.verifyProductVisible(updatedName, updatedPrice);
    }
}