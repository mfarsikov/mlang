import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("org.jetbrains.kotlin.jvm") version "1.3.50"
    id ("antlr")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    antlr("org.antlr:antlr4:4.7.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
        javaParameters = true
    }
}


tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
}
