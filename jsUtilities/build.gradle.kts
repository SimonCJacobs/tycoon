repositories {
    jcenter()
}

plugins {
    kotlin( "js" )
}

dependencies {
    implementation( kotlin( "stdlib-js" ) )
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