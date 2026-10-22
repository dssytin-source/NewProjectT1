import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class Homework6Lecture2Test {

    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = "http://localhost:8080";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 6000;
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    // 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Test
    @DisplayName("2.1. Добавление товара через админку")
    public void testAddProductViaAdmin() {
        String testProductName = "ItemSelenide_" + System.currentTimeMillis();
        String testProductPrice = "400";

        open("/");

        // Переходим в админку
        $("a[href='/admin']").shouldBe(visible).click();

        // Логинимся
        $("input[name='username']").shouldBe(visible).setValue("admin");
        $("input[name='password']").setValue("secret123");
        $("form.login-form button[type='submit']").click();

        // Заполняем форму добавления товара
        $(By.xpath("//input[@name='name' or @id='name' or contains(@placeholder, 'Название')]"))
                .shouldBe(visible)
                .setValue(testProductName);

        $(By.xpath("//input[@name='price' or @id='price' or contains(@placeholder, 'Цена')]"))
                .setValue(testProductPrice);

        $(By.xpath("//button[contains(., 'Добавить') or contains(., 'Создать') or @type='submit']"))
                .click();

        // Возвращаемся на витрину
        open("/");

        // Проверяем, что товар появился в списке
        $$(".product-card h4")
                .findBy(text(testProductName))
                .shouldBe(visible);
    }

    // 2.2. Добавить товар в корзину и проверить, что он отображается
    @Test
    @DisplayName("2.2. Добавление товара в корзину")
    public void testAddToCart() {
        open("/");

        // Берем имя первого товара на витрине
        String productName = $$(".product-card").first().shouldBe(visible).$("h4").text();

        // Жмем «В корзину» у первой карточки
        $$(".product-card").first().$("button[data-action='add-to-cart']").click();

        // Открываем корзину
        $("#open-cart-btn").shouldBe(visible).click();

        // Проверяем, что модалка открыта и внутри лежит наш товар
        $("#cartModal").shouldBe(visible);
        $("#cart-items").shouldHave(text(productName));
    }

    // 2.3. Попытаться войти в админку с неверным логином и паролем
    @Test
    @DisplayName("2.3. Вход с неверными логином и паролем")
    public void testLoginWithWrongCredentials() {
        open("/");

        // Идем в админку
        $("a[href='/admin']").shouldBe(visible).click();

        // Вводим некорректные данные
        $("input[name='username']").shouldBe(visible).setValue("badUser");
        $("input[name='password']").setValue("badPass");
        $("form.login-form button[type='submit']").click();

        // Проверяем средствами Selenide, что адрес остался со страницей логина
        webdriver().shouldHave(urlContaining("/login"));
    }

    // 2.4. Проверить сохранение товаров в корзине после обновления страницы
    @Test
    @Disabled("BUG: Корзина сбрасывается после перезагрузки F5")
    @DisplayName("2.4. Сохранение корзины после перезагрузки")
    public void testCartPersistenceAfterRefresh() {
        open("/");

        String productName = $$(".product-card").first().shouldBe(visible).$("h4").text();

        // Добавляем в корзину
        $$(".product-card").first().$("button[data-action='add-to-cart']").click();

        // Обновляем страницу (F5)
        refresh();

        // Открываем корзину
        $("#open-cart-btn").shouldBe(visible).click();

        // Проверяем модалку и наличие товара
        $("#cartModal").shouldBe(visible);
        $("#cart-items").shouldHave(text(productName));
    }

    // 2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ». Проверить JS Alert
    @Test
    @DisplayName("2.5. Проверка JS Alert при заказе свыше 300 рублей")
    public void testCheckoutWithAmountOver300ShowsAlert() {
        open("/");

        // Ждем загрузку товаров
        $(".product-card").shouldBe(visible);

        // Добавляем первый товар (в базе цена товара от 300 руб.)
        $$(".product-card").get(0).$("button[data-action='add-to-cart']").click();

        // Для гарантии суммы свыше 300 руб. добавляем еще один товар (второй в списке или тот же)
        if ($$(".product-card").size() > 1) {
            $$(".product-card").get(1).$("button[data-action='add-to-cart']").click();
        } else {
            $$(".product-card").get(0).$("button[data-action='add-to-cart']").click();
        }

        // Открываем окно корзины
        $("#open-cart-btn").shouldBe(visible).click();
        $("#cartModal").shouldBe(visible);

        // Нажимаем «Оформить заказ»
        $(By.xpath("//button[contains(., 'Оформить заказ')]")).shouldBe(visible).click();

        // Проверяем появление JS Alert и закрываем его через Selenide
        switchTo().alert().accept();
    }
}