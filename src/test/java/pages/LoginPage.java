package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("form.login-form button[type='submit']");

    public LoginPage openPage() {
        open("/admin");
        return this;
    }

    public LoginPage setUsername(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    public void clickSubmit() {
        submitButton.shouldBe(visible).click();
    }

    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickSubmit();
    }

    public LoginPage verifyUsernameVisible() {
        usernameInput.shouldBe(visible);
        return this;
    }

    public LoginPage verifyUsernameValue(String expectedUsername) {
        usernameInput.shouldHave(value(expectedUsername));
        return this;
    }

    public LoginPage verifyPasswordVisible() {
        passwordInput.shouldBe(visible);
        return this;
    }

    public LoginPage verifySubmitButtonVisible() {
        submitButton.shouldBe(visible);
        return this;
    }

    public LoginPage verifySubmitButtonText(String expectedText) {
        submitButton.shouldHave(text(expectedText));
        return this;
    }
}