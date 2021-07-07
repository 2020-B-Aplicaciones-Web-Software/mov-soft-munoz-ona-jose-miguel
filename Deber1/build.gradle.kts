import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "me.joset"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("no.tornado:tornadofx:1.7.20")
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

