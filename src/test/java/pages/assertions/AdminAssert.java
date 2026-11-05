package pages.assertions;

import io.qameta.allure.Step;
import pages.AdminPage;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class AdminAssert {

    private final AdminPage adminPage;

    public AdminAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @Step("Проверить, что товар '{productName}' присутствует в таблице админки с ценой {expectedPrice} руб.")
    public AdminAssert verifyProductInTable(String productName, int expectedPrice) {
        var row = adminPage.findRowByProductName(productName);
        row.$$("input").get(1).shouldHave(value(String.valueOf(expectedPrice)));
        return this;
    }

    @Step("Проверить, что товар '{productName}' успешно добавлен в таблицу")
    public AdminAssert verifyProductAddedNotification(String productName) {
        adminPage.findRowByProductName(productName).shouldBe(visible);
        return this;
    }
}