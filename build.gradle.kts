plugins {
    kotlin("jvm") version "2.3.20"
    application
    kotlin("plugin.serialization") version "2.3.20" apply false
    id("com.gradleup.shadow") version "9.4.0"
}

group = "com.nickcoblentz"
version = "0.2.0"

repositories {
    mavenCentral()

    maven(url="https://jitpack.io") {
        content {
            includeGroup("com.github.ncoblentz")
        }
    }
}

dependencies {
    //testImplementation(kotlin("test"))
    //testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
    implementation("com.github.ajalt.clikt:clikt-markdown:5.1.0")

    implementation("com.github.ajalt.mordant:mordant:3.0.2")
    implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.2")
    implementation("com.github.ajalt.mordant:mordant-markdown:3.0.2")

    implementation("io.fabric8:kubernetes-client:7.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.21.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.21.3")

    implementation("com.github.ncoblentz:PentestLibrary:v0.1.3")

    implementation("de.gesellix:docker-client:2026-03-11T08-30-00")
    implementation("io.ktor:ktor-client-core:3.4.3")
    implementation("io.ktor:ktor-client-cio:3.4.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.3")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")


    implementation("com.google.code.gson:gson:2.14.0")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(25)
}

application {
    mainClass.set("com.nickcoblentz.kubepentest.CLIKt")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.nickcoblentz.kubepentest.CLIKt"
        }
    }
    shadowJar {
        dependencies {
            exclude(dependency("com.kohlschutter.junixsocket:junixsocket-core:.*"))
        }
    }
}
