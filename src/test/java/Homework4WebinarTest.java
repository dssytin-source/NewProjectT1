import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Tag("api")
public class Homework4WebinarTest {

    private static RequestSpecification requestSpec;

    public static class ProductRequest {
        private String name;
        private Double price;

        public ProductRequest() {}

        public ProductRequest(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductResponse {
        private Long id;
        private String name;
        private Double price;

        public ProductResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";

        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080")
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.basic("admin", "secret123"))
                .addFilter(new AllureRestAssured())
                .build();
    }

    //Методы обращения к эндпоинтам с @Step

    @Step("POST /goods/add — Создание товара '{body.name}' с ценой {body.price}")
    private Response sendPostAddProduct(ProductRequest body) {
        return given().spec(requestSpec).body(body).when().post("/goods/add");
    }

    @Step("GET /goods/{id} — Получение информации о товаре по id={id}")
    private Response sendGetProductById(Object id) {
        return given().spec(requestSpec).when().get("/goods/{id}", id);
    }

    @Step("PATCH /goods/{id} — Частичное обновление товара id={id}")
    private Response sendPatchProduct(Object id, ProductRequest body) {
        return given().spec(requestSpec).body(body).when().patch("/goods/{id}", id);
    }

    @Step("DELETE /goods/{id} — Удаление товара id={id}")
    private Response sendDeleteProduct(Object id) {
        return given().spec(requestSpec).when().delete("/goods/{id}", id);
    }

    @Step("GET /goods/list — Получение списка товаров (страница={page}, размер={size})")
    private Response sendGetGoodsList(int page, int size) {
        return given().spec(requestSpec).queryParam("page", page).queryParam("size", size).when().get("/goods/list");
    }

    //API-проверки с @Step

    @Step("Проверка статус-кода ответа: ожидается {expectedStatus}")
    private void verifyStatusCode(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверка наличия данных в ответе (data != null)")
    private void verifyResponseHasData(Response response) {
        response.then().body("data", notNullValue());
    }

    @Step("Проверка поля 'price' в ответе: ожидается {expectedPrice}")
    private void verifyProductPrice(Response response, float expectedPrice) {
        response.then().body("price", is(expectedPrice));
    }

    @Step("Проверка соответствия ID товара ({expectedId}) и цены ({expectedPrice}) в объекте ответа")
    private void verifyProductResponseFields(ProductResponse actual, Long expectedId, Double expectedPrice) {
        assertThat(actual.getId()).as("ID товара").isEqualTo(expectedId);
        assertThat(actual.getPrice()).as("Цена товара").isEqualTo(expectedPrice);
    }

    @Step("Вспомогательный шаг: создание товара '{namePrefix}' для подготовки данных")
    private Long createProduct(String namePrefix, Double price) {
        String uniqueName = namePrefix + "_" + System.nanoTime();
        ProductRequest product = new ProductRequest(uniqueName, price);

        Response response = sendPostAddProduct(product);
        verifyStatusCode(response, 200);

        Object idObj = response.path("data.id");
        if (idObj == null) {
            idObj = sendGetGoodsList(0, 100)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("goods.find { it.name == '" + uniqueName + "' }.id");
        }

        assertThat(idObj).as("ID созданного товара не должен быть null").isNotNull();
        return ((Number) idObj).longValue();
    }

    @Test
    @DisplayName("POST /goods/add -> 200 OK: успешное добавление товара")
    void testAddProduct200() {
        String uniqueName = "Product_" + System.nanoTime();
        ProductRequest body = new ProductRequest(uniqueName, 599.0);

        Response response = sendPostAddProduct(body);
        verifyStatusCode(response, 200);
        verifyResponseHasData(response);
    }

    @Test
    @DisplayName("POST /goods/add -> 400 Bad Request: нарушение minimum: 0 для цены")
    void testAddProduct400() {
        ProductRequest invalidBody = new ProductRequest("NegativeItem_" + System.nanoTime(), -15.0);

        Response response = sendPostAddProduct(invalidBody);
        verifyStatusCode(response, 400);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 200 OK: получение товара по ID")
    void testGetProductById200() {
        Long productId = createProduct("GetItem", 1450.0);

        Response response = sendGetProductById(productId);
        verifyStatusCode(response, 200);

        ProductResponse product = response.as(ProductResponse.class);
        verifyProductResponseFields(product, productId, 1450.0);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404 Not Found: несуществующий ID")
    void testGetProductById404() {
        Long deletedId = createProduct("TempFor404", 100.0);

        Response delResp = sendDeleteProduct(deletedId);
        verifyStatusCode(delResp, 200);

        Response getResp = sendGetProductById(deletedId);
        verifyStatusCode(getResp, 404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200 OK: частичное обновление товара")
    void testPatchProduct200() {
        Long productId = createProduct("PatchItem", 1000.0);
        String updatedName = "PatchUpdated_" + System.nanoTime();
        ProductRequest updateBody = new ProductRequest(updatedName, 1999.0);

        Response response = sendPatchProduct(productId, updateBody);
        verifyStatusCode(response, 200);
        verifyProductPrice(response, 1999.0f);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400 Bad Request: невалидная цена при обновлении")
    void testPatchProduct400() {
        Long productId = createProduct("PatchBad", 1000.0);
        ProductRequest invalidUpdate = new ProductRequest("PatchBadUpdated_" + System.nanoTime(), -1.0);

        Response response = sendPatchProduct(productId, invalidUpdate);
        verifyStatusCode(response, 400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404 Not Found: обновление несуществующего ID")
    void testPatchProduct404() {
        int nonExistentId = 99999;
        ProductRequest validBody = new ProductRequest("GhostItem_" + System.nanoTime(), 500.0);

        Response response = sendPatchProduct(nonExistentId, validBody);
        verifyStatusCode(response, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 200 OK: успешное удаление товара")
    void testDeleteProduct200() {
        Long productId = createProduct("DeleteItem", 300.0);

        Response delResp = sendDeleteProduct(productId);
        verifyStatusCode(delResp, 200);

        Response getResp = sendGetProductById(productId);
        verifyStatusCode(getResp, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 Not Found: удаление несуществующего ID")
    void testDeleteProduct404() {
        int nonExistentId = 99999;

        Response response = sendDeleteProduct(nonExistentId);
        verifyStatusCode(response, 404);
    }

    @Test
    @DisplayName("GET /goods/list -> 200 OK: получение списка товаров с пагинацией")
    void testGetGoodsList200() {
        Response response = sendGetGoodsList(0, 10);
        verifyStatusCode(response, 200);
        response.then().body("goods", notNullValue());
    }
}