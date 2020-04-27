import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set( false )
}

tasks {
    withType < KotlinCompile > {
        kotlinOptions {
            freeCompilerArgs = listOf( "-Xopt-in=kotlin.ExperimentalDceDsl" )
        }
    }
}
