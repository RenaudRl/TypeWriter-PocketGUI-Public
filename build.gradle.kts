import org.gradle.api.file.DuplicatesStrategy
plugins {
    kotlin("jvm") version "2.2.10"
    id("com.typewritermc.module-plugin") version "2.0.0"
}

group = "btc.renaud"
version = "0.1" // The version is the same with the plugin to avoid confusion. :)

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    implementation("com.typewritermc:BasicExtension:0.9.0")
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

typewriter {
    namespace = "renaud"

    extension {
        name = "PocketGui"
        shortDescription = "Typewriter extension For Pocket Gui support."
        description =
            "This extension adds support for GUI to typewriter for open" +
            " easy different type of menus.more characters............."
        engineVersion = "0.9.0-beta-165"
        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA
        dependencies {
            dependency("typewritermc", "Basic")
        }
        paper()

    }

}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
