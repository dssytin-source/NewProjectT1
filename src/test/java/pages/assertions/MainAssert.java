package pages.assertions;

import pages.MainPage;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainAssert {

    private final MainPage mainPage;

    public MainAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public MainAssert verifyProductVisible(String productName, int productPrice) {
        var card = mainPage.getProductCards()
                .findBy(text(productName))
                .shouldBe(visible);
        card.shouldHave(text(String.valueOf(productPrice)));
        return this;
    }
}