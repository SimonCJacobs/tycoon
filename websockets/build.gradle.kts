import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

repositories {
    jcenter()
}

plugins {
    kotlin( "multiplatform" ) //version Versions.kotlin
    kotlin( "plugin.serialization" ) //version Versions.kotlin
}

kotlin {
    js {
        browser {
            dceTask {
                // See https://github.com/ktorio/ktor/issues/1399 and /1400 expect fix in kotlin 1.4.0
                keep( "ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io" )
                this.dceOptions.devMode = false
            }
            testTask {
                useMocha()
            }
            webpackTask {
                this.mode = Mode.DEVELOPMENT
            }
        }
    }

    jvm {
        compilations {
            val acceptanceTest by creating {
                tasks {
                    // TODO: Try and build acceptance tests. To make this work
                    // I think you need to explore how the build process calls javascript code
                    // and replicate it inside the jvmAcceptanceTest code
                    create( "acceptanceTest", Test::class ) {
                        val sourceSet = sourceSets.getByName( "jvmAcceptanceTest" )
                        classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                        description = "Acceptance tests"
                        group = "Verification"
                        testClassesDirs = output.classesDirs
                    }
                    check {
                        dependsOn( "acceptanceTest" )
                    }
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation( kotlin( "stdlib-common" ) )
                implementation( "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${ Versions.kotlinSerialization }" )
                implementation( "org.kodein.di:kodein-di-erased:${ Versions.kodein }"  )
                implementation( "io.ktor:ktor-client-core:${ Versions.ktor }" )
                implementation( "io.ktor:ktor-client-websockets:${ Versions.ktor }"  )
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
                implementation( kotlin( "stdlib-js" ) )
                implementation( project( ":jsUtilities" ) )
                implementation( "org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${ Versions.kotlinSerialization }" )
                implementation( "io.ktor:ktor-client-js:${ Versions.ktor }"  )
                implementation( "io.ktor:ktor-client-websockets-js:${ Versions.ktor }"  )
                    // See https://github.com/ktorio/ktor/issues/1400
                implementation( npm( "text-encoding", "0.7.0" ) )
                implementation( npm( "abort-controller", "3.0.0" ) )
            }
        }
        val jsTest by getting {
            dependencies {
                implementation( kotlin( "test-js" ) )
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation( kotlin( "stdlib-jdk8" ) )
                implementation( "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${ Versions.kotlinSerialization }" )
                implementation( "io.ktor:ktor-server-netty:${ Versions.ktor }" )
                implementation( "io.ktor:ktor-websockets:${ Versions.ktor }" )
                implementation( "ch.qos.logback:logback-classic:${ Versions.logback }" )
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation( kotlin( "test-junit" ) )
                implementation( "org.assertj:assertj-core:${ Versions.assertJ }" )
            }
        }
        val jvmAcceptanceTest by getting {
            //dependsOn( jvmMain )
            //dependsOn( jvmTest )
        }
    }

    tasks {
        withType < KotlinCompile > {
            kotlinOptions.jvmTarget = Versions.javaBytecode
        }
    }

}