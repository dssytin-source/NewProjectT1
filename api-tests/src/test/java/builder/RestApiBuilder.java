package builder;

import config.ConfigProvider;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestApiBuilder {

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigProvider.getApiUrl())
                .setContentType(ContentType.JSON)
                .setAuth(io.restassured.RestAssured.basic(
                        ConfigProvider.getAdminUsername(),
                        ConfigProvider.getAdminPassword()))
                .addFilter(new AllureRestAssured())
                .build();
    }
}