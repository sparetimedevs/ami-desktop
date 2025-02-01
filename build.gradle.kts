import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.decompose.jvm)
    implementation(libs.decompose.compose.jetbrains.jvm)
    implementation(libs.reaktive.jvm)
    implementation(libs.reaktive.coroutines.interop.jvm)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.arrow)

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.uiTest)

    // Required for pitest. A future version of the pitest gradle plugin may make this unnecessary.
    testRuntimeOnly(libs.junit.platform.launcher)

    pitest(libs.arcmutate.base)
    pitest(libs.arcmutate.pitest.kotlin.plugin)
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
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-receivers",
            "-Xbackend-threads=4",
        )
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}

configure<SpotlessExtension> {
    kotlin {
        target("**/kotlin/**/*.kt")
        ktlint("1.5.0")
        licenseHeaderFile("$rootDir/LICENSE.header.template")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.5.0")
    }
}

afterEvaluate {
    val spotlessApply = tasks.findByName("spotlessApply")
    tasks.withType<KotlinCompile> { dependsOn(spotlessApply) }
}
