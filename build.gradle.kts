plugins {
    java
    id("org.springframework.boot") version "2.7.10"
    id("io.spring.dependency-management") version "1.1.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.json:json:20220924")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.3")
    implementation("com.zaxxer:HikariCP")
    implementation("org.postgresql:postgresql")
    implementation("org.jdbi:jdbi3-core:3.41.3")
    implementation("org.jdbi:jdbi3-postgres:3.41.3")
    implementation("org.flywaydb:flyway-core:8.5.11")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
    implementation("it.sauronsoftware.cron4j:cron4j:2.2.5")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}