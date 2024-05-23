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
    implementation("io.vertx:vertx-web:4.5.7")
    implementation("io.vertx:vertx-core:4.0.0.CR2")
    implementation("io.vertx:vertx-web-client:4.0.0.CR2")

    implementation("io.github.java-native:jssc:2.9.2")
}

tasks.test {
    useJUnitPlatform()
}