import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Homework6Lecture1Test {

    WebDriver driver;
    WebDriverWait wait;
    final String BASE_URL = "http://localhost:8080";

    // 1.5. Инициализация браузера перед каждым тестом
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--log-level=3");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(6));
    }

    // 1.5. Закрытие браузера после каждого теста
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 1.1: Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Test
    @DisplayName("1.1. Добавление товара через админку")
    public void testAddProductViaAdmin() throws InterruptedException {
        String testProductName = "Item_" + System.currentTimeMillis();
        String testProductPrice = "350";

        driver.get(BASE_URL);

        // Переходим в админку по прямой ссылке в шапке
        WebElement adminLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/admin']")));
        adminLink.click();

        // Авторизация
        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        userInput.sendKeys("admin");

        WebElement passInput = driver.findElement(By.name("password"));
        passInput.sendKeys("secret123");

        driver.findElement(By.cssSelector("form.login-form button[type='submit']")).click();

        // Заполняем форму создания товара
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='name' or @id='name' or contains(@placeholder, 'Название')]")));
        nameInput.sendKeys(testProductName);

        WebElement priceInput = driver.findElement(
                By.xpath("//input[@name='price' or @id='price' or contains(@placeholder, 'Цена')]"));
        priceInput.sendKeys(testProductPrice);

        WebElement addBtn = driver.findElement(
                By.xpath("//button[contains(., 'Добавить') or contains(., 'Создать') or @type='submit']"));
        addBtn.click();

        Thread.sleep(1000); // пауза на запись в БД

        // Возвращаемся на главную
        driver.get(BASE_URL);

        // Проверяем наличие карточки с товаром
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card")));
        List<WebElement> productCards = driver.findElements(By.cssSelector(".product-card h4"));

        assertThat(productCards)
                .extracting(WebElement::getText)
                .contains(testProductName);
    }

    // 1.2: Добавить товар в корзину и проверить, что он отображается
    @Test
    @DisplayName("1.2. Добавление товара в корзину")
    public void testAddToCart() throws InterruptedException {
        driver.get(BASE_URL);

        // Ждем карточки и берем название первого товара
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card")));
        WebElement firstCard = driver.findElement(By.cssSelector(".product-card"));
        String productName = firstCard.findElement(By.cssSelector("h4")).getText();

        // Кликаем по кнопке «В корзину»
        WebElement addBtn = firstCard.findElement(By.cssSelector("button[data-action='add-to-cart']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        // Ждем обновления счетчика в шапке
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("cart-count"), "1"));

        // Открываем корзину по точному id кнопки
        driver.findElement(By.id("open-cart-btn")).click();

        // Проверяем, что товар появился в списке #cart-items
        WebElement cartItems = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-items")));
        assertThat(cartItems.getText()).contains(productName);
    }

    // 1.3: Попытаться войти в админку с неверным логином и паролем
    @Test
    @DisplayName("1.3. Вход с неверным логином и паролем")
    public void testLoginWithWrongCredentials() {
        driver.get(BASE_URL);

        WebElement adminLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/admin']")));
        adminLink.click();

        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        userInput.sendKeys("wrongUser");

        WebElement passInput = driver.findElement(By.name("password"));
        passInput.sendKeys("wrongPass");

        driver.findElement(By.cssSelector("form.login-form button[type='submit']")).click();

        // Проверяем, что остались на странице /login
        assertThat(driver.getCurrentUrl()).contains("login");
    }

    // 1.4: Проверить сохранение товаров в корзине после обновления страницы
    @Test
    @Disabled("BUG: Корзина сбрасывается после перезагрузки F5")
    @DisplayName("1.4. Сохранение корзины после перезагрузки")
    public void testCartSaveAfterRefresh() throws InterruptedException {
        driver.get(BASE_URL);

        // 1. Находим первую карточку и запоминаем название товара
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card")));
        WebElement card = driver.findElement(By.cssSelector(".product-card"));
        String expectedName = card.findElement(By.cssSelector("h4")).getText();

        // 2. Нажимаем «В корзину»
        WebElement addBtn = card.findElement(By.cssSelector("button[data-action='add-to-cart']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        Thread.sleep(1000); // даем фронтенду записать корзину в память

        // 3. Перезагружаем страницу (F5)
        driver.navigate().refresh();
        Thread.sleep(1200); // ждем повторной инициализации скриптов страницы

        // 4. Открываем окно корзины кликом по кнопке в шапке
        WebElement openCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("open-cart-btn")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openCartBtn);

        // Если с первого раза анимация не открыла модалку, делаем контрольный клик
        Thread.sleep(600);
        WebElement modal = driver.findElement(By.id("cartModal"));
        if (!modal.isDisplayed()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openCartBtn);
            Thread.sleep(600);
        }

        // 5. Ждем появления строки с товаром внутри корзины
        WebElement itemInCart = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#cart-items .cart-item")));

        // 6. Проверяем, что название товара совпадает
        assertThat(itemInCart.getText()).contains(expectedName);
    }
}