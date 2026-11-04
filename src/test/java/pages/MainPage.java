package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
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

    public MainPage openPage() {
        open("/");
        return this;
    }

    public MainPage clickOpenCart() {
        openCartButton.shouldBe(visible).click();
        return this;
    }

    public void clickAdminLink() {
        adminLink.shouldBe(visible).click();
    }

    public MainPage addFirstProductToCart() {
        firstProductAddToCartBtn.shouldBe(visible).click();
        return this;
    }

    public MainPage increaseFirstProductCount(int clicks) {
        var plusBtn = productCards.first().$$(".qty-controls button").last();
        for (int i = 0; i < clicks; i++) {
            plusBtn.click();
        }
        return this;
    }

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

    public MainPage verifyOpenCartButtonVisible() {
        openCartButton.shouldBe(visible);
        return this;
    }

    public MainPage verifyCartCountBadgeVisible() {
        cartCountBadge.shouldBe(visible);
        return this;
    }

    public MainPage verifyCartCount(String expectedCount) {
        cartCountBadge.shouldHave(text(expectedCount));
        return this;
    }

    public MainPage verifyAdminLinkVisible() {
        adminLink.shouldBe(visible);
        return this;
    }

    public MainPage verifyProductCardsLoaded() {
        productCards.shouldHave(sizeGreaterThan(0));
        return this;
    }

    public MainPage verifyFirstProductButtonText(String expectedText) {
        firstProductAddToCartBtn.shouldHave(text(expectedText));
        return this;
    }
}