package pages.assertions;

import pages.AdminPage;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class AdminAssert {

    private final AdminPage adminPage;

    public AdminAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    public AdminAssert verifyProductInTable(String productName, int expectedPrice) {
        var row = adminPage.findRowByProductName(productName);
        row.$$("input").get(1).shouldHave(value(String.valueOf(expectedPrice)));
        return this;
    }

    public AdminAssert verifyProductAddedNotification(String productName) {
        adminPage.findRowByProductName(productName).shouldBe(visible);
        return this;
    }
}