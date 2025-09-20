import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree.Companion.test
import java.time.*
import java.time.format.*

group = "org.reladev"
version = "0.1.0"
val artifact = "test.recorder.api"

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

    implementation(project(":kaptest-api"))
    implementation(libs.kotlinx.serialization.core.jvm)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines)

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
