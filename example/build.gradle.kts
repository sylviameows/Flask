plugins {
    id("java")
//    id("io.papermc.paperweight.userdev") version "1.7.1" // paperweight userdev
}

description = "duels example plugin for flask"
version = "0.1-ALPHA"

group = "io.github.sylviameows"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // paper

    maven("https://repo.infernalsuite.com/repository/maven-snapshots/") // slime worlds
    maven("https://repo.rapture.pw/repository/maven-releases/") // flow-nbt fix
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly(project(":api"))

    compileOnly("com.infernalsuite.aswm:api:3.0.0-SNAPSHOT") // slime worlds api
}


tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}