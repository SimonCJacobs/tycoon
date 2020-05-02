import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
}

plugins {
    kotlin( "multiplatform" )
    kotlin( "plugin.serialization" )
}

kotlin {
    js {
        browser {
            testTask {
                useMocha()
            }
        }
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api( kotlin( "stdlib-common" ) )
                implementation( project( ":websockets" ) )
                implementation( "org.kodein.di:kodein-di-erased:${ Versions.kodein }"  )
            }
        }
        val commonTest by getting {
            dependencies {
                implementation( kotlin( "test-common" ) )
                implementation( kotlin( "test-annotations-common" ) )
            }
        }
        val jsMain by getting {
            dependencies {
                api( kotlin( "stdlib-js" ) )
            }
        }
        val jsTest by getting {
            dependencies {
                implementation( kotlin( "test-js" ) )
            }
        }
        val jvmMain by getting {
            dependencies {
                api( kotlin( "stdlib-jdk8" ) )
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation( kotlin( "test-junit" ) )
            }
        }
    }

}

tasks {
    withType < KotlinCompile > {
        kotlinOptions.jvmTarget = Versions.javaBytecode
    }
}