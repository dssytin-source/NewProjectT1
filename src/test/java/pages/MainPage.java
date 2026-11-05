package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    private final SelenideElement openCartButton = $("#open-cart-btn");
    private final SelenideElement cartCountBadge = $("#cart-count");
    private final SelenideElement adminLink = $("a[href='/admin']");
    private final ElementsCollection productCards = $$(".product-card");
    private final SelenideElement firstProductAddToCartBtn = $$(".product-card").first().$("button[data-action='add-to-cart']");

    @Step("Открыть витрину магазина '/'")
    public MainPage openPage() {
        open("/");
        return this;
    }

    @Step("Нажать на кнопку корзины в шапке")
    public MainPage clickOpenCart() {
        openCartButton.shouldBe(visible).click();
        return this;
    }

    @Step("Перейти по ссылке в панель администрирования")
    public void clickAdminLink() {
        adminLink.shouldBe(visible).click();
    }

    @Step("Добавить первый товар витрины в корзину")
    public MainPage addFirstProductToCart() {
        firstProductAddToCartBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Увеличить количество первого товара на {clicks} шт.")
    public MainPage increaseFirstProductCount(int clicks) {
        var plusBtn = productCards.first().$$(".qty-controls button").last();
        for (int i = 0; i < clicks; i++) {
            plusBtn.click();
        }
        return this;
    }

    @Step("Добавить товар '{productName}' в корзину в количестве {quantity} шт.")
    public MainPage addProductToCartByName(String productName, int quantity) {
        SelenideElement card = productCards.findBy(text(productName)).shouldBe(visible);
        var plusBtn = card.$$(".qty-controls button").last();
        for (int i = 1; i < quantity; i++) {
            plusBtn.click();
        }
        card.$("button[data-action='add-to-cart']").click();
        return this;
    }

    public ElementsCollection getProductCards() {
        return productCards;
    }

    @Step("Проверить отображение кнопки корзины")
    public MainPage verifyOpenCartButtonVisible() {
        openCartButton.shouldBe(visible);
        return this;
    }

    @Step("Проверить отображение счетчика товаров в корзине")
    public MainPage verifyCartCountBadgeVisible() {
        cartCountBadge.shouldBe(visible);
        return this;
    }

    @Step("Проверить значение счетчика корзины: '{expectedCount}'")
    public MainPage verifyCartCount(String expectedCount) {
        cartCountBadge.shouldHave(text(expectedCount));
        return this;
    }

    @Step("Проверить отображение ссылки на админку")
    public MainPage verifyAdminLinkVisible() {
        adminLink.shouldBe(visible);
        return this;
    }

    @Step("Убедиться, что карточки товаров загрузились на витрине")
    public MainPage verifyProductCardsLoaded() {
        productCards.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Проверить текст кнопки у первой карточки: '{expectedText}'")
    public MainPage verifyFirstProductButtonText(String expectedText) {
        firstProductAddToCartBtn.shouldHave(text(expectedText));
        return this;
    }
}