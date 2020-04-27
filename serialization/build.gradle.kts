import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

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
            dceTask {
                // See https://github.com/ktorio/ktor/issues/1400
                keep( "ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io" )
                this.dceOptions.devMode = false
            }
            webpackTask {
                this.mode = Mode.DEVELOPMENT
            }
        }
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation( kotlin( "stdlib-common" ) )
            }
        }
        val jsMain by getting {
            dependencies {
                ( kotlin( "stdlib-js" ) )
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation( kotlin( "stdlib-jdk8" ) )
            }
        }

    }

}

tasks {
    withType < KotlinCompile > {
        kotlinOptions.jvmTarget = Versions.javaBytecode
    }
}

