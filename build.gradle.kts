import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.spotless)
    alias(libs.plugins.pitest)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.ami.music.sdk.kotlin)
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.decompose.jvm)
    implementation(libs.decompose.compose.jetbrains.jvm)
    implementation(libs.reaktive.jvm)
    implementation(libs.reaktive.coroutines.interop.jvm)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.arrow)

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) implementation(compose.uiTest)

    // Required for pitest. A future version of the pitest gradle plugin may make this unnecessary.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")

    pitest("com.arcmutate:base:1.3.0")
    pitest("com.arcmutate:pitest-kotlin-plugin:1.2.2")
}

pitest {
    targetClasses.set(listOf("com.sparetimedevs.ami.*"))
    pitestVersion.set("1.15.3")
    junit5PluginVersion.set("1.2.0")
    outputFormats.set(listOf("XML", "HTML"))
    exportLineCoverage.set(true)
    features.set(listOf("+exclusion")) // See file resources/exclusions.pitest
}

tasks.withType<Test>().configureEach { useJUnitPlatform() }

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
        jvmTarget = "21"
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
