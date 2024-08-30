plugins {
    id("java")
}

group = "io.github.sylviameows"
version = "0.5-ALPHA"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // paper
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
//    testImplementation(platform("org.junit:junit-bom:5.9.1"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.test {
    useJUnitPlatform()
}