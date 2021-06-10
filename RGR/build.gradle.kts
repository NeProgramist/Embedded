import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven("https://dl.bintray.com/mipt-npm/dataforge")
    maven("https://dl.bintray.com/mipt-npm/kscience")
    maven("https://dl.bintray.com/kotlin/kotlin-datascience")
}
dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("kscience.plotlykt:plotlykt-server:0.3.0")
    implementation("org.jetbrains:kotlin-numpy:0.1.5")

}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
