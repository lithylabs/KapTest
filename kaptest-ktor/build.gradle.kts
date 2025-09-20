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
    implementation(project(":kaptest-api"))
    implementation(project(":kaptest-core"))

    // Bundles
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)
    implementation(libs.kodein.di)

    // Libraries
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.tc.postgresql)

    // Testing
    testImplementation(libs.bundles.testing)
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
