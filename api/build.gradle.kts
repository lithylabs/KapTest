import java.time.*
import java.time.format.*

group = "org.reladev"
version = "0.13.0"

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
    implementation(libs.kotlinx.serialization.core.jvm)
    implementation(libs.kotlinx.serialization.json)
}

group = "org.reladev"
val artifact = "test.recorder.api"
version = "0.1.0_dev"

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



//val ktor_version = "3.0.3"
//
//val logback_version="1.4.11"
//val exposed_version = "0.43.0"
//
//
//dependencies {
//    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
//    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.5.0")
//
//    api("org.kodein.di:kodein-di-generic-jvm:6.5.5")
//    api("joda-time:joda-time:2.10.6")
//
//    implementation("org.mindrot:jbcrypt:0.4")
//
//    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
//    implementation("io.ktor:ktor-server-cors:$ktor_version")
//    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
//    api("io.ktor:ktor-client-okhttp:$ktor_version")
//    api("io.ktor:ktor-client-json-jvm:$ktor_version")
//    //api("io.ktor:ktor-client-serialization-jvm:$ktor_version") {
//    //    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
//    //    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json")
//
//    implementation("ch.qos.logback:logback-classic:$logback_version")
//
//    //Database
//    implementation("org.postgresql:postgresql:42.6.0")
//    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")
//    implementation("com.mchange:c3p0:0.9.5.5")
//
//    //Test
//    api("io.kotest:kotest-assertions-core-jvm:5.6.2")
//    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
//    api("org.flywaydb:flyway-core:9.8.1")
//    testImplementation("org.testcontainers:postgresql:1.19.3")
//
//
//
//
//
//}
//
//
////============================================================================
//// Standard Build Configuration
////============================================================================
//
////val service = project.extensions.getByType<JavaToolchainService>()
////val customLauncher = service.launcherFor {
////    languageVersion.set(JavaLanguageVersion.of(17))
////}
////project.tasks.withType<UsesKotlinJavaToolchain>().configureEach {
////    kotlinJavaToolchain.toolchain.use(customLauncher)
////}
//
//plugins {
//    kotlin("jvm") version "1.9.21"
//    kotlin("plugin.serialization") version "1.9.21"
//    id("io.ktor.plugin") version "2.3.7"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("org.flywaydb.flyway") version "9.8.1"
//}
//
//repositories {
//    mavenCentral()
//}
//
//tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
//    archiveFileName.set("page-service-all.jar")
//}
//
//tasks.jar {
//    doLast {
//        val date = Instant.now().atZone(ZoneId.of("America/Los_Angeles")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
//        File("build/libs/version.json").writeText(
//            """
//                {
//                    "version":"$version",
//                    "date": "$date"
//                }
//                   """.trimIndent()
//        )
//    }
//}
