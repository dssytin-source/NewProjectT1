import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class Homework9LectureTest {

    private final MainPage mainPage = new MainPage();
    private final LoginPage loginPage = new LoginPage();

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
    @DisplayName("Проверка элементов и взаимодействия на LoginPage")
    public void testLoginPageElementsAndInteractions() {
        loginPage.openPage()
                .verifyUsernameVisible()
                .verifyPasswordVisible()
                .verifySubmitButtonVisible()
                .verifySubmitButtonText("Sign in")
                .setUsername(ConfigProvider.getAdminUsername())
                .verifyUsernameValue(ConfigProvider.getAdminUsername())
                .setPassword(ConfigProvider.getAdminPassword())
                .clickSubmit();
    }

    @Test
    @DisplayName("Проверка элементов и взаимодействия на MainPage")
    public void testMainPageElementsAndInteractions() {
        mainPage.openPage()
                .verifyOpenCartButtonVisible()
                .verifyAdminLinkVisible()
                .verifyProductCardsLoaded()
                .verifyFirstProductButtonText("В корзину")
                .addFirstProductToCart()
                .verifyCartCountBadgeVisible()
                .verifyCartCount("1")
                .clickOpenCart();
    }
}