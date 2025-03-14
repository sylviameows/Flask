plugins {
    checkstyle
}

allprojects {
    group = "io.github.sylviameows"
    version = "0.10.0-ALPHA"

    apply(plugin = "checkstyle")

    checkstyle {
        toolVersion = "10.21.4"
    }
}

task("version") {
    println(project.version);
}