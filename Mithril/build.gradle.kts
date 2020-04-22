repositories {
    jcenter()
}

plugins {
    kotlin( "js" ) version Versions.kotlin
}

dependencies {
    implementation( kotlin( "stdlib-js" ) )
    implementation( npm( "mithril" ) )
    implementation( project( ":jsUtilities", "JsDefault" ) )
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