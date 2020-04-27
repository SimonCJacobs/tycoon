repositories {
    jcenter()
}


plugins {
        // These are added due to some issue with the Kotlin plugins https://github.com/Kotlin/kotlinx.serialization/issues/499
    kotlin( "multiplatform" ) version Versions.kotlin apply false
    kotlin( "plugin.serialization" ) version Versions.kotlin apply false
}