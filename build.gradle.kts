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