plugins {
    kotlin("jvm") version "1.8.22"
    id("priv.seventeen.artist.blink") version "1.1.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

blink {
    name.set("Overture")
    version.set("1.0.0")
    description.set("Next-generation item library plugin")
    authors.set(listOf("17Artist"))
    packageName.set("priv.seventeen.artist.overture")
    apiVersion.set("1.18")
    kotlinVersion.set("1.8.22")
    logPrefix.set("§b♪ §dOverture")
    depend.set(listOf())
    softDepend.set(listOf())
    foliaSupported.set(false)
    enableAsteroid.set(true)
    enableAria.set(true)
}

repositories {
    maven("https://repo.arcartx.com/repository/maven-public/")
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("priv.seventeen.artist.blink:blink-common:1.1.1")
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.named("build") {
    dependsOn("shadowJar")
}
