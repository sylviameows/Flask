pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "Flask"
include("core")
var core = project(":core")

include("api")
var api = project(":api")

core.name = "flask-core"
api.name = "flask-api"

include("example")
