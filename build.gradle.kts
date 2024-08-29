plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.1" // paperweight userdev
    id("xyz.jpenilla.run-paper") version "2.3.0" // run paper server plugin
}

description = "minigame wrapper plugin for 1.21.1"
version = "0.4-ALPHA"

group = "io.github.sylviameows"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // paper
    maven("https://repo.infernalsuite.com/repository/maven-snapshots/") // slime worlds
    maven("https://repo.rapture.pw/repository/maven-releases/") // flow-nbt fix
}

dependencies {
    compileOnly("com.infernalsuite.aswm:api:3.0.0-SNAPSHOT") // slime worlds api
    implementation("com.infernalsuite.aswm:loaders:3.0.0-SNAPSHOT") // slime world loaders

    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT") // paper dependency

    // fawe requirement for region manipulation
//    implementation(platform("com.intellectualsites.bom:bom-newest:1.45")) // Ref: https://github.com/IntellectualSites/bom
//    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
//    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit")
}

// defines usage of mojang mappings
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
    assemble {
        dependsOn(reobfJar) // can be removed when updating to post 1.20.4
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21) // push to 21 when updating past 1.20.4
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

    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    runServer {
        minecraftVersion("1.20.4")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}