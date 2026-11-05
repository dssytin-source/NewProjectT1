plugins {
    id("io.qameta.allure")
}

dependencies {
    testImplementation(project(":common"))

    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    testImplementation("io.qameta.allure:allure-junit5:2.27.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.27.0")
}

tasks.register<Test>("smokeApiTest") {
    description = "Smoke-прогон API-тестов (@Tag('smoke'))"
    group = "verification"
    useJUnitPlatform {
        includeTags("smoke")
    }
    testLogging {
        showStandardStreams = true
    }
}