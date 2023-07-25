import java.net.URI

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}


dependencyResolutionManagement {
//    enableFeaturePreview("VERSION_CATALOGS")
    versionCatalogs {
        create("myLibs") {
            from(files("/gradle/libs.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://cardinalcommerceprod.jfrog.io/artifactory/android")

                credentials {
                    username = "paypal_sgerritz"
                    password = "AKCp8jQ8tAahqpT5JjZ4FRP2mW7GMoFZ674kGqHmupTesKeAY2G8NcmPKLuTxTGkKjDLRzDUQ"
                }
            }
        
    }

}
rootProject.name = "shopify"
include (":app")