package decoder

plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    testLogging {
        events("passed", "skipped", "failed")
    }
    useJUnitPlatform()
}
