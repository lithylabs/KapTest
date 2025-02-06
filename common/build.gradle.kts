import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree.Companion.test
import java.time.*
import java.time.format.*

group = "org.reladev"
version = "0.1.0"
val artifact = "test.recorder.core"

plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":api"))
    implementation(project(":core"))

    implementation(libs.kodein.di)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.mock)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)

}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = artifact
            version = version.toString()
        }
    }
    repositories {
        mavenLocal()
    }
}
