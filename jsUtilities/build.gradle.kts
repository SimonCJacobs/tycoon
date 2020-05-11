repositories {
    jcenter()
}

plugins {
    kotlin( "js" )
}

dependencies {
    implementation( kotlin( "stdlib-js" ) )
   /// api( "org.jetbrains:kotlin-extensions:${ Versions.kotlinJsWrappers }" )
    testImplementation( kotlin( "test-js" ) )
}

kotlin {
    target {
        browser {
            dceTask {
                enabled = false
            }
            testTask {
                useMocha()
            }
            webpackTask {
                enabled = false
            }
        }
    }

}