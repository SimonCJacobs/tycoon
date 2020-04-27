import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
}

plugins {
    kotlin( "jvm" )
    application
}

application {
    mainClassName = "jacobs.tycoon.application.MainKt"
}

dependencies {
    implementation( kotlin( "stdlib-jdk8" ) )
    implementation( project( ":shared", "jvmDefault" ) )
    implementation( project( ":websockets" ) )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:${ Versions.kotlinCoroutines }" )
}

tasks {
    withType < JavaExec > {
        standardOutput = System.`out`
    }
    withType < KotlinCompile > {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=io.ktor.util.KtorExperimentalAPI",
                "-Xopt-in=kotlinx.serialization.ImplicitReflectionSerializer"
            )
            jvmTarget = Versions.javaBytecode
        }
    }
}