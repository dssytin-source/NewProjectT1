import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@Tag("api")
public class Homework4LectureTest {

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

    @Test
    @DisplayName("1.1. Базовый запрос given-when-then на получение товаров")
    void testGetGoodsListGivenWhenThen() {
        given()
                .auth().basic("admin", "secret123")
                .contentType(ContentType.JSON)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());
    }

    @Test
    @DisplayName("1.2. Запрос списка товаров с использованием RequestSpecification")
    void testGetGoodsListWithSpec() {
        given()
                .spec(requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue());
    }

    @Test
    @DisplayName("1.3. Добавление товара и проверка списка через матчеры REST Assured")
    void testAddAndVerifyWithRestAssured() {
        String testItemName = "LectureItem_RA_" + System.nanoTime();
        ProductRequest product = new ProductRequest(testItemName, 120.0);

        given()
                .spec(requestSpec)
                .body(product)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200);

        given()
                .spec(requestSpec)
                .queryParam("page", 0)
                .queryParam("size", 100)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", hasItem(testItemName));
    }

    @Test
    @DisplayName("1.4. Добавление товара и проверка списка через AssertJ")
    void testAddAndVerifyWithAssertJ() {
        String testItemName = "LectureItem_AssertJ_" + System.nanoTime();
        ProductRequest product = new ProductRequest(testItemName, 250.0);

        given()
                .spec(requestSpec)
                .body(product)
                .when()
                .post("/goods/add")
                .then()
                .statusCode(200);

        List<ProductResponse> products = given()
                .spec(requestSpec)
                .queryParam("page", 0)
                .queryParam("size", 100)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("goods", ProductResponse.class);

        assertThat(products)
                .as("Список товаров должен содержать созданный элемент")
                .extracting(ProductResponse::getName)
                .contains(testItemName);
    }
}