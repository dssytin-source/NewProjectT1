import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class Homework6WebinarTest {

    private final String BASE_URL = "http://localhost:8080";
    private final List<Integer> createdProductIds = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = BASE_URL;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 8000;

        RestAssured.baseURI = BASE_URL;
    }

    // Критерий 3.6: Очистка созданных тестовых данных в блоке @AfterEach через API
    @AfterEach
    public void tearDown() {
        if (!createdProductIds.isEmpty()) {
            String sessionId = getAdminSessionId();
            for (Integer id : createdProductIds) {
                try {
                    RestAssured.given()
                            .sessionId(sessionId)
                            .contentType(ContentType.JSON)
                            .body("{\"id\":" + id + "}")
                            .when()
                            .post("/goods/delete");
                } catch (Exception ignored) {
                }
            }
            createdProductIds.clear();
        }
        closeWebDriver();
    }

    // Вспомогательный метод для получения JSESSIONID администратора
    private String getAdminSessionId() {
        return RestAssured.given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "admin")
                .formParam("password", "secret123")
                .when()
                .post("/login")
                .then()
                .statusCode(anyOf(is(200), is(302)))
                .extract()
                .sessionId();
    }

    // Критерий 3.5: Генерация тестовых данных через API (POST /goods/add)
    private void createProductViaApi(String name, int price) {
        String sessionId = getAdminSessionId();
        String requestBody = String.format("{\"name\":\"%s\",\"price\":%d}", name, price);

        var response = RestAssured.given()
                .sessionId(sessionId)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract();

        try {
            Integer id = response.path("id");
            if (id != null) {
                createdProductIds.add(id);
            }
        } catch (Exception ignored) {
        }
    }

    // 3.1. Добавить три единицы товара в корзину и оплатить их (сумма <= 300 руб.). Проверить уведомление об обработке заказа
    @Test
    @DisplayName("3.1. Покупка 3 единиц товара до 300 руб. и проверка уведомления")
    public void testBuyThreeItemsUnder300() {
        String cheapItem = "CheapPen_" + System.currentTimeMillis();
        // 3.5: Создаем товар по 50 руб. через API (3 шт. = 150 руб., что <= 300 руб.)
        createProductViaApi(cheapItem, 50);

        open("/");

        var productCard = $$(".product-card").findBy(text(cheapItem)).shouldBe(visible);

        // Увеличиваем счетчик до 3 кликами по кнопке [+]
        var plusBtn = productCard.$$(".qty-controls button").last();
        plusBtn.click();
        sleep(200);
        plusBtn.click();
        sleep(200);

        // Добавляем 3 шт. в корзину
        productCard.$("button[data-action='add-to-cart']").click();

        // Открываем модальное окно корзины
        $("#open-cart-btn").shouldBe(visible).click();
        $("#cartModal").shouldBe(visible);

        // Нажимаем «Оформить заказ»
        $(By.xpath("//button[contains(., 'Оформить заказ')]")).shouldBe(visible).click();

        // Проверяем уведомление об обработке заказа согласно критерию 3.1
        // Проверяем заголовок «Заказ успешно оформлен!» или всплывающее сообщение «Заказ принят в обработку!»
        $(By.xpath("//*[contains(text(), 'Заказ успешно оформлен') or contains(text(), 'Заказ принят в обработку')]"))
                .shouldBe(visible);
    }

    // 3.2. Добавить в корзину несколько разных товаров и проверить корректность подсчета общей цены
    @Test
    @DisplayName("3.2. Проверка подсчета итоговой стоимости нескольких товаров")
    public void testCartTotalCalculation() {
        String item1 = "Book_" + System.currentTimeMillis();
        String item2 = "Notepad_" + System.currentTimeMillis();

        createProductViaApi(item1, 120);
        createProductViaApi(item2, 180);

        open("/");

        $$(".product-card").findBy(text(item1)).$("button[data-action='add-to-cart']").click();
        sleep(500);
        $$(".product-card").findBy(text(item2)).$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").shouldBe(visible).click();
        $("#cartModal").shouldBe(visible).shouldHave(text("300"));
    }

    // 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара
    @Test
    @DisplayName("3.3. Добавление товара через админку и проверка уведомления")
    public void testAddProductShowsNotification() {
        String productName = "AdminItem_" + System.currentTimeMillis();

        open("/admin");

        $("input[name='username']").shouldBe(visible).setValue("admin");
        $("input[name='password']").setValue("secret123");
        $("form.login-form button[type='submit']").click();

        $(By.xpath("//input[@name='name' or @id='name' or contains(@placeholder, 'Название')]"))
                .shouldBe(visible)
                .setValue(productName);

        $(By.xpath("//input[@name='price' or @id='price' or contains(@placeholder, 'Цена')]"))
                .setValue("250");

        $(By.xpath("//button[contains(., 'Добавить') or contains(., 'Создать') or @type='submit']"))
                .click();

        $(By.xpath("//*[contains(@class, 'alert') or contains(@class, 'notification') or contains(@class, 'toast') or contains(., 'успешно') or contains(., 'добавлен')]"))
                .shouldBe(visible);
    }

    // 3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить изменения
    @Test
    @DisplayName("3.4. Редактирование товара в админке и проверка изменений на витрине")
    public void testEditProductAndVerifyOnStorefront() {
        String originalName = "EditTarget_" + System.currentTimeMillis();
        String updatedName = "UpdatedTarget_" + System.currentTimeMillis();

        // Создаем товар через API
        createProductViaApi(originalName, 200);

        open("/admin");

        // Авторизация
        $("input[name='username']").shouldBe(visible).setValue("admin");
        $("input[name='password']").setValue("secret123");
        $("form.login-form button[type='submit']").click();

        // Находим input по значению атрибута value и берем родительскую строку tr
        var nameInput = $("input[value='" + originalName + "']").shouldBe(visible);
        var row = nameInput.closest("tr");

        // Вводим новое название товара
        nameInput.clear();
        nameInput.setValue(updatedName);

        // Нажимаем «Сохранить» в этой же строке
        row.$(By.xpath(".//button[contains(., 'Сохранить')]")).click();
        sleep(1000); // пауза на сохранение изменений бэкендом

        // Открываем витрину и проверяем обновленное название товара
        open("/");
        $$(".product-card h4")
                .findBy(text(updatedName))
                .shouldBe(visible);
    }
}