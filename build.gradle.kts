plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta11"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

group = "io.github.lumine1909"
version = "1.2.4"
description = "Minecraft Note Block Tuning Plugin"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.52"))
    compileOnly("com.intellectualsites.plotsquared:plotsquared-core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("org.reflections:reflections:0.10.2")
    compileOnly("org.xerial:sqlite-jdbc:3.49.1.0")
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("PaperBlockTuner-${version}-MC-1.21.4.jar")
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
            "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
