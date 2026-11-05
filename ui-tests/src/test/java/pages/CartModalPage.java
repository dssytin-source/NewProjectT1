package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartModalPage {

    private final SelenideElement modal = $("#cartModal");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement checkoutButton = $("#makeOrder");
    private final SelenideElement toastContainer = $("#toast-container");

    @Step("Нажать кнопку 'Оформить заказ' в корзине")
    public CartModalPage clickCheckout() {
        checkoutButton.shouldBe(visible).click();
        return this;
    }

    public SelenideElement getModal() {
        return modal;
    }

    public SelenideElement getTotalPrice() {
        return totalPrice;
    }

    public SelenideElement getToastContainer() {
        return toastContainer;
    }
}