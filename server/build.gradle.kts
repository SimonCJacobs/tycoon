repositories {
    jcenter()
}

plugins {
    kotlin( "jvm" ) version Versions.kotlin
    application
}

application {
    mainClassName = "jacobs.tycoon.application.MainKt"
}

dependencies {
    implementation( kotlin( "stdlib-jdk8" ) )
    implementation( "io.ktor:ktor-server-netty:${ Versions.ktor }" )
    implementation( "io.ktor:ktor-websockets:${ Versions.ktor }" )
}

tasks {
    val run by getting( JavaExec::class ) {
        standardOutput = System.`out`
    }
}