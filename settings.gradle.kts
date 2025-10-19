pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "OSMTest"
include(":app")
include(":feature:map")
include(":core")
include(":data:point")
include(":data:weather")
include(":domain:point")
include(":domain:weather")
include(":feature:map:data")
include(":feature:map:domain")
include(":feature:mapDetails")
include(":feature:mapDetails:domain")
