import org.gradle.kotlin.dsl.annotationProcessor

plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.lotnest.dernbot"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

extra["jdaVersion"] = "5.3.1"
extra["gsonVersion"] = "2.12.1"
extra["guavaVersion"] = "33.4.0-jre"
extra["hikariCPVersion"] = "6.2.1"
extra["dotenv-javaVersion"] = "3.2.0"
extra["jda-commandsVersion"] = "4.0.0-beta.5"
extra["commons-lang3Version"] = "3.17.0"
extra["mapstructVersion"] = "1.6.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("net.dv8tion:JDA:${property("jdaVersion")}") {
        exclude(module = "opus-java")
    }
    implementation("com.google.code.gson:gson:${property("gsonVersion")}")
    implementation("com.google.guava:guava:${property("guavaVersion")}")
    implementation("com.zaxxer:HikariCP:${property("hikariCPVersion")}")
    implementation("io.github.cdimascio:dotenv-java:${property("dotenv-javaVersion")}")
    implementation("io.github.kaktushose:jda-commands:${property("jda-commandsVersion")}") {
        exclude(module = "guice-extension")
    }
    implementation("org.apache.commons:commons-lang3:${property("commons-lang3Version")}")
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
