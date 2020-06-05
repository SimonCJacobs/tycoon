import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

repositories {
    jcenter()
}

plugins {
    kotlin( "js" )
    id( "jacobs.deployclient" )
}

dependencies {
    implementation( project( ":shared", "jsDefault" ) )
    implementation( project( ":jsUtilities", "JsDefault" ) )
    implementation( project( ":mithril", "JsDefault" ) )
    implementation( project( ":websockets" )  )
    implementation( "org.kodein.di:kodein-di-erased:${ Versions.kodein }"  )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${ Versions.kotlinCoroutines }" )
}

var d: File? = null

kotlin {
    target {
        browser {
            dceTask {
                this.dceOptions.devMode = false
                // See https://github.com/ktorio/ktor/issues/1399 and /1400 expect fix in kotlin 1.4.0
                keep( "ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io" )
            }
            webpackTask {
                mode = Mode.DEVELOPMENT
            }
        }
    }
}

deployment {
    domain = "monopolisation.grayv.co.uk"
    region = software.amazon.awssdk.regions.Region.EU_WEST_2
    secretsDirectory = rootProject.layout.projectDirectory.dir( "secrets" )
    client {
        files += arrayOf( "index.html", "main.css" )
    }
}

tasks {
    withType( Kotlin2JsCompile::class ) {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=io.ktor.util.KtorExperimentalAPI",
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlin.js.ExperimentalJsExport"
            )
        }
    }
    register < Copy > ( "copyCss" ) {
        from( "src/main/css" )
        into( "build/distributions" )
    }
    assemble {
        dependsOn( "copyCss" )
    }
    deploy {
        dependsOn ( "copyCss" )
    }
}