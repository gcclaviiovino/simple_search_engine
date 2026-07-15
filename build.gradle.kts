plugins {
    kotlin("jvm") version "2.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(26)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runEngine") {
    group = "application"
    description = "Runs the search engine with arguments"

    dependsOn("classes")
    mainClass.set("MainKt")

    // Use project.the to safely extract the runtime classpath
    val sourceSets = project.the<SourceSetContainer>()
    classpath = sourceSets["main"].runtimeClasspath

    standardInput = System.`in`
}