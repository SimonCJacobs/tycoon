import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

repositories {
    jcenter()
}

plugins {
    kotlin( "js" ) version Versions.kotlin
}

dependencies {
    implementation( project( ":shared", "jsDefault" ) )
    implementation( project( ":jsUtilities", "JsDefault" ) )
    implementation( project( ":mithril", "JsDefault" ) )
    implementation( "io.ktor:ktor-client-core:${ Versions.ktor }"  )
    implementation( "io.ktor:ktor-client-js:${ Versions.ktor }"  )
    implementation( "io.ktor:ktor-client-websockets:${ Versions.ktor }"  )
        // See https://github.com/ktorio/ktor/issues/1400
    implementation( npm( "text-encoding", "0.7.0" ) )
    implementation( npm( "abort-controller", "3.0.0" ) )
}

kotlin {
    target {
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
}

tasks {
    withType( Kotlin2JsCompile::class ) {
        kotlinOptions {
            allWarningsAsErrors = true
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlin.js.ExperimentalJsExport",
                "-Xopt-in=io.ktor.util.KtorExperimentalAPI"
            )
        }
    }
}