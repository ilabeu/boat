plugins {
    id 'fabric-loom' version '1.14.10'
}

group = 'com.ilabeu'
version = '0.2.0'

repositories {
    maven { url 'https://maven.meteordev.org/releases' }
    maven { url 'https://maven.meteordev.org/snapshots' }
    mavenCentral()
}

dependencies {
    minecraft 'com.mojang:minecraft:1.21.4'
    mappings 'net.fabricmc:yarn:1.21.4+build.7:v2'
    modImplementation 'net.fabricmc:fabric-loader:0.16.9'
    modImplementation 'net.fabricmc.fabric-api:fabric-api:0.119.4+1.21.4'
    modImplementation 'meteordevelopment:meteor-client:0.5.9'
}

processResources {
    inputs.property 'version', project.version
    filteringCharset 'UTF-8'

    filesMatching('fabric.mod.json') {
        expand 'version': project.version
    }
}

tasks.withType(JavaCompile).configureEach {
    it.options.release = 21
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
