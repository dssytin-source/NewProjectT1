package assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.ProductResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ApiAssert {

    @Step("Проверить код статуса ответа: {expectedStatus}")
    public static void assertStatusCode(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверить наличие данных в ответе (data != null)")
    public static void assertDataNotNull(Response response) {
        response.then().body("data", notNullValue());
    }

    @Step("Проверить поле 'price' в ответе: {expectedPrice}")
    public static void assertPrice(Response response, float expectedPrice) {
        response.then().body("price", is(expectedPrice));
    }

    @Step("Проверить поля объекта ProductResponse: id={expectedId}, price={expectedPrice}")
    public static void assertProductFields(ProductResponse product, Long expectedId, Double expectedPrice) {
        assertThat(product.getId()).isEqualTo(expectedId);
        assertThat(product.getPrice()).isEqualTo(expectedPrice);
    }
}