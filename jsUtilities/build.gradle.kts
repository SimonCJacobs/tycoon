repositories {
    jcenter()
}

plugins {
    kotlin( "js" ) version Versions.kotlin
}

dependencies {
    implementation( kotlin( "stdlib-js" ) )
}

kotlin {
    target {
        browser {
            dceTask {
                enabled = false
            }
            webpackTask {
                enabled = false
            }
        }
    }
}