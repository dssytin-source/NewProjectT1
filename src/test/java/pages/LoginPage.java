package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("form.login-form button[type='submit']");

    @Step("Открыть форму логина /admin")
    public LoginPage openPage() {
        open("/admin");
        return this;
    }

    @Step("Ввести логин: '{username}'")
    public LoginPage setUsername(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    @Step("Нажать кнопку входа")
    public void clickSubmit() {
        submitButton.shouldBe(visible).click();
    }

    @Step("Авторизоваться администратором с логином '{username}'")
    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSubmit();
    }

    @Step("Проверить видимость поля логина")
    public LoginPage verifyUsernameVisible() {
        usernameInput.shouldBe(visible);
        return this;
    }

    @Step("Проверить значение поля логина: '{expectedUsername}'")
    public LoginPage verifyUsernameValue(String expectedUsername) {
        usernameInput.shouldHave(value(expectedUsername));
        return this;
    }

    @Step("Проверить видимость поля пароля")
    public LoginPage verifyPasswordVisible() {
        passwordInput.shouldBe(visible);
        return this;
    }

    @Step("Проверить видимость кнопки отправки формы")
    public LoginPage verifySubmitButtonVisible() {
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Проверить текст кнопки отправки: '{expectedText}'")
    public LoginPage verifySubmitButtonText(String expectedText) {
        submitButton.shouldHave(text(expectedText));
        return this;
    }
}