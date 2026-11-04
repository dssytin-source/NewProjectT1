package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$ ;
import static com.codeborne.selenide.Selenide.open;

public class AdminPage {

    private final SelenideElement productNameInput = $("input[placeholder='Название']");
    private final SelenideElement productPriceInput = $("input[placeholder='Цена']");
    private final SelenideElement createButton = $$("button").findBy(text("Создать"));
    private final SelenideElement toStorefrontLink = $$("a").findBy(text("Вернуться на сайт"));

    public AdminPage openPage() {
        open("/admin");
        return this;
    }

    public AdminPage enterProductName(String name) {
        productNameInput.shouldBe(visible).clear();
        productNameInput.setValue(name);
        return this;
    }

    public AdminPage enterProductPrice(int price) {
        productPriceInput.shouldBe(visible).clear();
        productPriceInput.setValue(String.valueOf(price));
        return this;
    }

    public AdminPage clickSaveProduct() {
        createButton.shouldBe(visible).click();
        return this;
    }

    public SelenideElement findRowByProductName(String productName) {
        return $(org.openqa.selenium.By.xpath("//tbody[@id='tbody']//tr[.//input[@value='" + productName + "']]")).shouldBe(visible);
    }

    public AdminPage editProductInTable(String oldName, String newName, int newPrice) {
        SelenideElement row = findRowByProductName(oldName);

        row.$("input[id^='nm-']").clear();
        row.$("input[id^='nm-']").setValue(newName);

        row.$$("input").get(1).clear();
        row.$$("input").get(1).setValue(String.valueOf(newPrice));

        row.$("button.btn-upd").shouldBe(visible).click();
        return this;
    }

    public void goToStorefront() {
        toStorefrontLink.shouldBe(visible).click();
    }
}