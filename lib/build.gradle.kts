plugins {
    id("decoder.java-conventions")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")

    implementation("ch.qos.logback:logback-classic:1.2.3")
}