plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // paper
    maven("https://repo.infernalsuite.com/repository/maven-snapshots/") // slime worlds
    maven("https://repo.rapture.pw/repository/maven-releases/") // flow-nbt fix
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT") // paper
    compileOnly("com.infernalsuite.asp:api:4.0.0-SNAPSHOT") // slime worlds api
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}