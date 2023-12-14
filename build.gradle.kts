import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.10"
    id("com.diffplug.spotless") version "6.22.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(compose.desktop.currentOs)
    val arrowVersion = "1.2.1"
    val amiMusicDomainVersion = "0.0.1-SNAPSHOT"
    val kotlinxCoroutinesVersion = "1.7.3"
    val decomposeVersion = "2.1.4"
    val reaktiveVersion = "1.3.0"
    val kotestVersion = "5.8.0"
    val kotestAssertionsArrowVersion = "1.4.0"
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("com.sparetimedevs.ami:type-safe-music-domain:$amiMusicDomainVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("com.arkivanov.decompose:decompose-jvm:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains-jvm:$decomposeVersion")
    implementation("com.badoo.reaktive:reaktive-jvm:$reaktiveVersion")
    implementation("com.badoo.reaktive:coroutines-interop-jvm:$reaktiveVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:$kotestAssertionsArrowVersion")
}

tasks.withType<Test>().configureEach { useJUnitPlatform() }

// compose.desktop {
//    application {
//        mainClass = "MainKt"
//    }
// }
compose.desktop {
    application {
        mainClass = "com.sparetimedevs.ami.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ami-for-desktop"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs =
            listOf(
                "-Xcontext-receivers",
                //            "-Xuse-k2",
                "-Xbackend-threads=4",
            )
        jvmTarget = "11"
        languageVersion = "1.8"
    }
}

configure<SpotlessExtension> {
    kotlin {
        target("**/kotlin/**/*.kt")
        ktfmt().kotlinlangStyle()
        licenseHeaderFile("$rootDir/LICENSE.header.template")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }
}

afterEvaluate {
    val spotlessApply = tasks.findByName("spotlessApply")
    tasks.withType<KotlinCompile> { dependsOn(spotlessApply) }
}
