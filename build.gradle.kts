plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
        finalizedBy ("finishTests")
}
tasks.register("finishTests") {
    doLast {
        println ("Test run is over")
    }
}
