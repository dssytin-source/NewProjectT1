package tests;

import api.GoodsApi;
import assertions.ApiAssert;
import models.ProductRequest;
import models.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
public class GoodsApiTest {

    private final GoodsApi goodsApi = new GoodsApi();

    private Long createProduct(String namePrefix, Double price) {
        String uniqueName = namePrefix + "_" + System.nanoTime();
        ProductRequest product = new ProductRequest(uniqueName, price);

        var response = goodsApi.createProduct(product);
        ApiAssert.assertStatusCode(response, 200);

        Object idObj = response.path("data.id");
        if (idObj == null) {
            idObj = goodsApi.getGoodsList(0, 100)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("goods.find { it.name == '" + uniqueName + "' }.id");
        }

        assertThat(idObj).as("ID созданного товара не должен быть null").isNotNull();
        return ((Number) idObj).longValue();
    }

    @Test
    @Tag("smoke")
    @DisplayName("POST /goods/add -> 200 OK: успешное добавление товара")
    void testAddProduct200() {
        String uniqueName = "Product_" + System.nanoTime();
        ProductRequest body = new ProductRequest(uniqueName, 599.0);

        var response = goodsApi.createProduct(body);
        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertDataNotNull(response);
    }

    @Test
    @DisplayName("POST /goods/add -> 400 Bad Request: нарушение minimum: 0 для цены")
    void testAddProduct400() {
        ProductRequest invalidBody = new ProductRequest("NegativeItem_" + System.nanoTime(), -15.0);

        var response = goodsApi.createProduct(invalidBody);
        ApiAssert.assertStatusCode(response, 400);
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /goods/{id} -> 200 OK: получение товара по ID")
    void testGetProductById200() {
        Long productId = createProduct("GetItem", 1450.0);

        var response = goodsApi.getProductById(productId);
        ApiAssert.assertStatusCode(response, 200);

        ProductResponse product = response.as(ProductResponse.class);
        ApiAssert.assertProductFields(product, productId, 1450.0);
    }

    @Test
    @DisplayName("GET /goods/{id} -> 404 Not Found: несуществующий ID")
    void testGetProductById404() {
        Long deletedId = createProduct("TempFor404", 100.0);
        var delResp = goodsApi.deleteProduct(deletedId);
        ApiAssert.assertStatusCode(delResp, 200);

        var getResp = goodsApi.getProductById(deletedId);
        ApiAssert.assertStatusCode(getResp, 404);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 200 OK: частичное обновление товара")
    void testPatchProduct200() {
        Long productId = createProduct("PatchItem", 1000.0);
        String updatedName = "PatchUpdated_" + System.nanoTime();
        ProductRequest updateBody = new ProductRequest(updatedName, 1999.0);

        var response = goodsApi.updateProduct(productId, updateBody);
        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertPrice(response, 1999.0f);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 400 Bad Request: невалидная цена при обновлении")
    void testPatchProduct400() {
        Long productId = createProduct("PatchBad", 1000.0);
        ProductRequest invalidUpdate = new ProductRequest("PatchBadUpdated_" + System.nanoTime(), -1.0);

        var response = goodsApi.updateProduct(productId, invalidUpdate);
        ApiAssert.assertStatusCode(response, 400);
    }

    @Test
    @DisplayName("PATCH /goods/{id} -> 404 Not Found: обновление несуществующего ID")
    void testPatchProduct404() {
        int nonExistentId = 99999;
        ProductRequest validBody = new ProductRequest("GhostItem_" + System.nanoTime(), 500.0);

        var response = goodsApi.updateProduct(nonExistentId, validBody);
        ApiAssert.assertStatusCode(response, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 200 OK: успешное удаление товара")
    void testDeleteProduct200() {
        Long productId = createProduct("DeleteItem", 300.0);

        var delResp = goodsApi.deleteProduct(productId);
        ApiAssert.assertStatusCode(delResp, 200);

        var getResp = goodsApi.getProductById(productId);
        ApiAssert.assertStatusCode(getResp, 404);
    }

    @Test
    @DisplayName("DELETE /goods/{id} -> 404 Not Found: удаление несуществующего ID")
    void testDeleteProduct404() {
        int nonExistentId = 99999;

        var response = goodsApi.deleteProduct(nonExistentId);
        ApiAssert.assertStatusCode(response, 404);
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /goods/list -> 200 OK: получение списка товаров с пагинацией")
    void testGetGoodsList200() {
        var response = goodsApi.getGoodsList(0, 10);
        ApiAssert.assertStatusCode(response, 200);
    }
}