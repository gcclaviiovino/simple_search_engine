plugins {
    kotlin("jvm") version "2.4.0"
    id("org.springframework.boot") version "3.5.16"
    kotlin("plugin.spring") version "2.4.0"
    kotlin("plugin.jpa") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.16"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.projectreactor:reactor-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}

allOpen {
    annotation("jakarta.persistence.*")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runEngine") {
    group = "application"
    description = "Runs the search engine with arguments"

    dependsOn("classes")
    mainClass.set("org.microservice.SearchEngineApplicationKt")

    val sourceSets = project.the<SourceSetContainer>()
    classpath = sourceSets["main"].runtimeClasspath

    standardInput = System.`in`
}