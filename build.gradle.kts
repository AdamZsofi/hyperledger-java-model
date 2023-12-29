plugins {
    id("java")
    id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20231013")

}

tasks.test {
    useJUnitPlatform()
}
