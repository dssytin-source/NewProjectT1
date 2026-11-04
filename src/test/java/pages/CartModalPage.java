package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CartModalPage {

    private final SelenideElement modal = $("#cartModal");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement checkoutButton = $("#makeOrder");
    private final SelenideElement toastContainer = $("#toast-container");

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