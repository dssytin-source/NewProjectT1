package pages.assertions;

import io.qameta.allure.Step;
import pages.CartModalPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartAssert {

    private final CartModalPage cartPage;

    public CartAssert(CartModalPage cartPage) {
        this.cartPage = cartPage;
    }

    @Step("Проверить, что модальное окно корзины отображается")
    public CartAssert verifyModalIsOpen() {
        cartPage.getModal().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что общая сумма в корзине равна {expectedSum} руб.")
    public CartAssert verifyTotalSumEquals(int expectedSum) {
        cartPage.getTotalPrice().shouldBe(visible).shouldHave(text(String.valueOf(expectedSum)));
        return this;
    }

    @Step("Проверить, что общая сумма в корзине не превышает {maxLimit} руб.")
    public CartAssert verifyTotalSumDoesNotExceed(int maxLimit) {
        String text = cartPage.getTotalPrice().shouldBe(visible).text().trim();
        int actual = Integer.parseInt(text);
        assertTrue(actual <= maxLimit, "Сумма заказа (" + actual + ") превышает лимит " + maxLimit);
        return this;
    }

    @Step("Проверить появление всплывающего уведомления о принятии заказа")
    public CartAssert verifyOrderNotificationShown() {
        cartPage.getToastContainer().shouldBe(visible);
        return this;
    }
}