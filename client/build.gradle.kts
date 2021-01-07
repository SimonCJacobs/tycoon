import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

repositories {
    jcenter()
}

plugins {
    kotlin( "js" )
    id( "jacobs.deployclient" )
    id( "jacobs-private-repo-plugin" )
}

dependencies {
    implementation( project( ":shared", "jsDefault" ) )
    implementation( "org.kodein.di:kodein-di-erased:${ Versions.kodein }"  )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${ Versions.kotlinCoroutines }" )
    implementation( group = "jacobs", name = "kotlin-websockets-js", version = Versions.kotlinWebsockets )
    implementation( group = "jacobs", name = "kotlin-js-utilities-js", version = Versions.jsUtilities )
    implementation( group = "jacobs", name = "mithril-kotlin-js", version = Versions.mithrilKotlin )
}

kotlin {
    target {
        browser {
            webpackTask {
                mode = Mode.DEVELOPMENT // toggled into production by deploy task
            }
        }
    }
}

deployment {
    domain = "monopolisation.grayv.co.uk"
    region = software.amazon.awssdk.regions.Region.EU_WEST_2
    secretsDirectory = rootProject.layout.projectDirectory.dir( "secrets" )
    client {
        files += arrayOf( "index.html", "admin.html", "main.css" )
    }
}

tasks {
    withType( Kotlin2JsCompile::class ) {
        kotlinOptions {
            freeCompilerArgs = listOf(
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
    register < Copy > ( "createAdminHTML" ) {
        from( "src/main/resources/index.html" )
        into( "build/distributions" )
        rename( "index.html", "admin.html" )
    }
    assemble {
        dependsOn( "copyCss", "createAdminHTML" )
    }
    deploy {
        dependsOn ( "copyCss", "createAdminHTML" )
    }
}