package assertions;

import io.qameta.allure.Step;
import pages.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainAssert {

    private final MainPage mainPage;

    public MainAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Step("Проверить, что товар '{productName}' с ценой {productPrice} руб. отображается на витрине")
    public MainAssert verifyProductVisible(String productName, int productPrice) {
        var card = mainPage.getProductCards()
                .findBy(text(productName))
                .shouldBe(visible);
        card.shouldHave(text(String.valueOf(productPrice)));
        return this;
    }
}