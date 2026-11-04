package pages.assertions;

import pages.CartModalPage;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartAssert {

    private final CartModalPage cartPage;

    public CartAssert(CartModalPage cartPage) {
        this.cartPage = cartPage;
    }

    public CartAssert verifyModalIsOpen() {
        cartPage.getModal().shouldBe(visible);
        return this;
    }

    public CartAssert verifyTotalSumEquals(int expectedSum) {
        cartPage.getTotalPrice().shouldBe(visible).shouldHave(text(String.valueOf(expectedSum)));
        return this;
    }

    public CartAssert verifyTotalSumDoesNotExceed(int maxLimit) {
        String text = cartPage.getTotalPrice().shouldBe(visible).text().trim();
        int actual = Integer.parseInt(text);
        assertTrue(actual <= maxLimit, "Сумма заказа (" + actual + ") превышает лимит " + maxLimit);
        return this;
    }

    public CartAssert verifyOrderNotificationShown() {
        cartPage.getToastContainer().shouldBe(visible);
        return this;
    }
}