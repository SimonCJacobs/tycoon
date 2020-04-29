repositories {
    jcenter()
}

plugins {
    kotlin( "js" )
}

dependencies {
    implementation( kotlin( "stdlib-js" ) )
    implementation( npm( "mithril", Versions.mithril ) )
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