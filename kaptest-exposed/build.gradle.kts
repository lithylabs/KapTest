import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree.Companion.test
import java.time.*
import java.time.format.*

group = "org.lithylabs.kaptest"
version = "0.1.0"
val artifact = "kaptest-exposed"

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
    implementation(libs.bundles.exposed)

    // Libraries
    implementation(libs.tc.postgresql)

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.h2)
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
