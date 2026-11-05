plugins {
    id("io.qameta.allure")
}

dependencies {
    testImplementation(project(":common"))

    testImplementation("com.codeborne:selenide:7.2.3")
    testImplementation("io.qameta.allure:allure-junit5:2.27.0")
    testImplementation("io.qameta.allure:allure-selenide:2.27.0")
}

tasks.register<Test>("smokeUiTest") {
    description = "Smoke-прогон UI-тестов (@Tag('smoke'))"
    group = "verification"
    useJUnitPlatform {
        includeTags("smoke")
    }
    testLogging {
        showStandardStreams = true
    }
}