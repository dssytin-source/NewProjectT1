import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.DragAndDropOptions.to;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class Homework7LectureTest {

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = BASE_URL;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 6000;
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    // 1.1 Перетащить элемент в корзину с помощью Drag-and-Drop
    @Test
    @DisplayName("1.1. Перетаскивание товара в корзину через Drag-and-Drop")
    public void testDragAndDropProductToCart() {
        open("/");

        // Находим первую карточку товара и сохраняем ее название
        var productCard = $$(".product-card").first().shouldBe(visible);
        String productName = productCard.$("h4").text();

        // Целевой элемент — кнопка открытия корзины в шапке
        var cartTarget = $("#open-cart-btn").shouldBe(visible);

        // Перетаскиваем карточку товара в корзину
        productCard.dragAndDrop(to(cartTarget));

        // Открываем корзину и проверяем, что товар появился в списке
        cartTarget.click();
        $("#cartModal").shouldBe(visible);
        $("#cart-items").shouldHave(text(productName));
    }

    // 1.2 Удалить добавленный элемент из корзины и проверить, что он там больше не отображается
    @Test
    @DisplayName("1.2. Удаление товара из корзины")
    public void testRemoveProductFromCart() {
        open("/");

        // Добавляем первый товар в корзину
        var productCard = $$(".product-card").first().shouldBe(visible);
        String productName = productCard.$("h4").text();
        productCard.$("button[data-action='add-to-cart']").click();

        // Открываем модальное окно корзины
        $("#open-cart-btn").shouldBe(visible).click();
        $("#cartModal").shouldBe(visible);

        // Находим строку добавленного товара в корзине
        var cartItem = $(".cart-item").shouldBe(visible).shouldHave(text(productName));

        // Нажимаем на кнопку удаления (красный крестик справа в строке)
        cartItem.$$("button").last().click();

        // Проверяем, что товар больше не отображается в списке корзины
        $("#cart-items").shouldNotHave(text(productName));
    }
}