import kotlin.Suppress
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
}

plugins {
    kotlin( "multiplatform" )
    kotlin( "plugin.serialization" )
    id( "jacobs-private-repo-plugin" )
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

    @Suppress( "UNUSED_VARIABLE" )
    sourceSets {
        forEach {
            it.languageSettings.apply {
                useExperimentalAnnotation( "kotlin.ExperimentalStdlibApi" )
            }
        }
        val commonMain by getting {
            dependencies {
                api( kotlin( "stdlib-common" ) )
                implementation( "jacobs:kotlin-websockets:${ Versions.kotlinWebsockets }" )
                implementation( "org.kodein.di:kodein-di-erased:${ Versions.kodein }"  )
            }
        }
        val commonTest by getting {
            dependencies {
                implementation( kotlin( "test-common" ) )
                implementation( kotlin( "test-annotations-common" ) )
                implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:${ Versions.kotlinCoroutines }" )
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
                implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${ Versions.kotlinCoroutines }" )
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
                implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:${ Versions.kotlinCoroutines }" )
            }
        }
    }

}

tasks {
    withType < KotlinCompile > {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.ExperimentalStdlibApi"
            )
            jvmTarget = Versions.javaBytecode
        }

    }
}