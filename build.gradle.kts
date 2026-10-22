plugins {
    id("java")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.19.1")
    implementation("io.rest-assured:rest-assured:5.4.0")
    implementation("org.assertj:assertj-core:3.25.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    testImplementation("com.codeborne:selenide:7.2.3")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
    finalizedBy("finishTests")
}

tasks.register<Test>("webinarTest") {
    description = "Runs webinar tests with tag 'webinar'"
    group = "verification"
    useJUnitPlatform {
        includeTags("homeworkWebinar")
    }
    testLogging {
        showStandardStreams = true
    }
    finalizedBy("finishTests")
}

tasks.register<Test>("lectureTest") {
    description = "Runs lecture tests with tag 'lecture'"
    group = "verification"
    useJUnitPlatform {
        includeTags("homeworkLecture")
    }
    testLogging {
        showStandardStreams = true
    }
    finalizedBy("finishTests")
}

tasks.register("finishTests") {
    doLast {
        println("Test run is over")
    }
}

tasks.register<Test>("apiTest") {
    description = "Запуск API-автотестов"
    group = "verification"
    useJUnitPlatform {
        includeTags("api")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}