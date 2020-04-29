repositories {
    jcenter()
}


plugins {
    kotlin( "multiplatform" ) version Versions.kotlin apply false
    kotlin( "plugin.serialization" ) version Versions.kotlin apply false
}