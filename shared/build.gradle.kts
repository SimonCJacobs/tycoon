repositories {
    jcenter()
}

plugins {
    kotlin( "multiplatform" ) version Versions.kotlin
}

kotlin {
    js {
        browser()
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api( kotlin( "stdlib-common" ) )
            }
        }
        val jvmMain by getting {
            dependencies {
                api( kotlin( "stdlib-jdk8" ) )
            }
        }
        val jsMain by getting {
            dependencies {
                api( kotlin( "stdlib-js" ) )
            }
        }
    }

}

