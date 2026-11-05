package api;

import builder.RestApiBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.ProductRequest;

import static io.restassured.RestAssured.given;

public class GoodsApi {

    @Step("POST /goods/add — Создать товар '{request.name}' с ценой {request.price}")
    public Response createProduct(ProductRequest request) {
        return given()
                .spec(RestApiBuilder.getRequestSpec())
                .body(request)
                .when()
                .post("/goods/add");
    }

    @Step("GET /goods/{id} — Получить товар по id={id}")
    public Response getProductById(Object id) {
        return given()
                .spec(RestApiBuilder.getRequestSpec())
                .when()
                .get("/goods/{id}", id);
    }

    @Step("PATCH /goods/{id} — Обновить товар id={id}")
    public Response updateProduct(Object id, ProductRequest request) {
        return given()
                .spec(RestApiBuilder.getRequestSpec())
                .body(request)
                .when()
                .patch("/goods/{id}", id);
    }

    @Step("DELETE /goods/{id} — Удалить товар id={id}")
    public Response deleteProduct(Object id) {
        return given()
                .spec(RestApiBuilder.getRequestSpec())
                .when()
                .delete("/goods/{id}", id);
    }

    @Step("GET /goods/list — Получить список товаров (page={page}, size={size})")
    public Response getGoodsList(int page, int size) {
        return given()
                .spec(RestApiBuilder.getRequestSpec())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/goods/list");
    }
}