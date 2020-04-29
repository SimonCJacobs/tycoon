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
        browser()
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

tasks {
    withType < KotlinCompile > {
        kotlinOptions.jvmTarget = Versions.javaBytecode
    }
}