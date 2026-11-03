import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class Homework8WebinarTest {

    // Критерий 1.3: Вывод параметров перед тестами в консоль
    @BeforeAll
    public static void beforeAll() {
        ConfigProvider.printConfig();
    }

    // Критерий 1.2: Инициализация параметров Selenide и API из конфига
    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = ConfigProvider.getAppUrl();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = ConfigProvider.getTimeout();

        RestAssured.baseURI = ConfigProvider.getApiUrl();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    // Вспомогательный метод авторизации с Credentials из конфига
    private String getAdminSessionId() {
        return RestAssured.given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", ConfigProvider.getAdminUsername())
                .formParam("password", ConfigProvider.getAdminPassword())
                .when()
                .post("/login")
                .then()
                .statusCode(anyOf(is(200), is(302)))
                .extract()
                .sessionId();
    }

    @Test
    @DisplayName("Создание стартового товара через API из конфига и проверка на витрине")
    public void testCreateConfigProductAndVerifyOnStorefront() {
        String name = ConfigProvider.getProductName() + "_" + System.currentTimeMillis();
        int price = ConfigProvider.getProductPrice();

        // Создаем стартовый товар через API
        String sessionId = getAdminSessionId();
        String requestBody = String.format("{\"name\":\"%s\",\"price\":%d}", name, price);

        RestAssured.given()
                .sessionId(sessionId)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        // Открываем витрину и проверяем созданный товар
        open("/");
        var card = $$(".product-card")
                .findBy(text(name))
                .shouldBe(visible);

        card.shouldHave(text(String.valueOf(price)));
    }
}