plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

group = "io.github.lumine1909"
version = "1.7.1"
description = "Minecraft Note Block Tuning Plugin"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
    implementation("io.github.lumine1909:messageutil:1.1.1")
    implementation("io.github.lumine1909:reflexion:0.5.2")
    implementation(files("libs/Proxying-1.0.1.jar"))
    compileOnly("com.intellectualsites.plotsquared:plotsquared-core:7.5.11")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.15.3")
    compileOnly("org.xerial:sqlite-jdbc:3.49.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("PaperBlockTuner-${version}+MC-26.2.jar")
        minimize()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "26.2"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
