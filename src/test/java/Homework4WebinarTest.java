import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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
                .build();
    }

    private Long createProduct(String namePrefix, Double price) {
        String uniqueName = namePrefix + "_" + System.nanoTime();
        ProductRequest product = new ProductRequest(uniqueName, price);

        Object idObj = given()
                .spec(requestSpec)
                .body(product)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .extract()
                .path("data.id");

        if (idObj == null) {
            idObj = given()
                    .spec(requestSpec)
                    .queryParam("page", 0)
                    .queryParam("size", 100)
                    .when()
                    .get("/goods/list")
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

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("POST /goods/add -> 400 Bad Request: нарушение minimum: 0 для цены")
    void testAddProduct400() {
        ProductRequest invalidBody = new ProductRequest("NegativeItem_" + System.nanoTime(), -15.0);

        given()
                .spec(requestSpec)
                .body(invalidBody)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 200 OK: получение товара по ID")
    void testGetProductById200() {
        Long productId = createProduct("GetItem", 1450.0);

        ProductResponse response = given()
                .spec(requestSpec)
                .when()
                .get("/goods/{id}", productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId.intValue()))
                .extract()
                .as(ProductResponse.class);

        assertThat(response.getId()).isEqualTo(productId);
        assertThat(response.getPrice()).isEqualTo(1450.0);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404 Not Found: несуществующий ID")
    void testGetProductById404() {
        Long deletedId = createProduct("TempFor404", 100.0);
        given()
                .spec(requestSpec)
                .when()
                .delete("/goods/{id}", deletedId)
                .then()
                .statusCode(200);

        given()
                .spec(requestSpec)
                .when()
                .get("/goods/{id}", deletedId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200 OK: частичное обновление товара")
    void testPatchProduct200() {
        Long productId = createProduct("PatchItem", 1000.0);
        String updatedName = "PatchUpdated_" + System.nanoTime();
        ProductRequest updateBody = new ProductRequest(updatedName, 1999.0);

        given()
                .spec(requestSpec)
                .body(updateBody)
                .when()
                .patch("/goods/{id}", productId)
                .then()
                .statusCode(200)
                .body("price", is(1999.0f));
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400 Bad Request: невалидная цена при обновлении")
    void testPatchProduct400() {
        Long productId = createProduct("PatchBad", 1000.0);
        ProductRequest invalidUpdate = new ProductRequest("PatchBadUpdated_" + System.nanoTime(), -1.0);

        given()
                .spec(requestSpec)
                .body(invalidUpdate)
                .when()
                .patch("/goods/{id}", productId)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404 Not Found: обновление несуществующего ID")
    void testPatchProduct404() {
        int nonExistentId = 99999;
        ProductRequest validBody = new ProductRequest("GhostItem_" + System.nanoTime(), 500.0);

        given()
                .spec(requestSpec)
                .body(validBody)
                .when()
                .patch("/goods/{id}", nonExistentId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 200 OK: успешное удаление товара")
    void testDeleteProduct200() {
        Long productId = createProduct("DeleteItem", 300.0);

        given()
                .spec(requestSpec)
                .when()
                .delete("/goods/{id}", productId)
                .then()
                .statusCode(200);

        given()
                .spec(requestSpec)
                .when()
                .get("/goods/{id}", productId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 Not Found: удаление несуществующего ID")
    void testDeleteProduct404() {
        int nonExistentId = 99999;

        given()
                .spec(requestSpec)
                .when()
                .delete("/goods/{id}", nonExistentId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /goods/list -> 200 OK: получение списка товаров с пагинацией")
    void testGetGoodsList200() {
        given()
                .spec(requestSpec)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());
    }
}