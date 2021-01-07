rootProject.name = "tycoon"

buildscript {
    repositories {
        maven {
            name = "jacobs-maven-public"
            url = java.net.URI( "http://jacobs-maven-public.s3-website.eu-west-2.amazonaws.com" )
        }
    }
    dependencies {
        classpath( "jacobs.plugins:jacobs-repo-plugin:1.0.0" )
    }
}

apply( plugin = "jacobs-repo-plugin" )

include(
    "client",
    "server",
    "shared"
)