plugins {
    id("fabric-loom") version "1.14.10"
    id("maven-publish")
}

version = "0.2.0"
group = property("maven_group") as String

base {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    maven {
        name = "meteor-maven"
        url = uri("https://maven.meteordev.org/releases")
    }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.4")
    mappings("net.fabricmc:yarn:1.21.4+build.7:v2")
    modImplementation("net.fabricmc:fabric-loader:0.16.9")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.119.4+1.21.4")

    // Meteor Client
    modImplementation("meteordevelopment:meteor-client:+:dev")

    // Annotation processors for Meteor's event system - CRITICAL FIX
    annotationProcessor("org.ow2.asm:asm:9.7")
    annotationProcessor("org.ow2.asm:asm-tree:9.7")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile> {
    options.release.set(21)
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}
