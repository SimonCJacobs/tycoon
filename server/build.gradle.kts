import software.amazon.awssdk.regions.Region
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    jcenter()
}

plugins {
    kotlin( "jvm" )
    application
    id( "jacobs.deployserver" )
}

application {
    mainClassName = "jacobs.tycoon.MainKt"
}

version = "0.0.1"

dependencies {
    implementation( kotlin( "stdlib-jdk8" ) )
    implementation( kotlin( "reflect" ) )
    implementation( project( ":shared", "jvmDefault" ) )
    implementation( project( ":websockets" ) )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:${ Versions.kotlinCoroutines }" )
    testImplementation( "org.junit.jupiter:junit-jupiter-api:${ Versions.jUnit }" )
    testRuntimeOnly( "org.junit.jupiter:junit-jupiter-engine:${ Versions.jUnit }" )
    testImplementation( "org.assertj:assertj-core:${ Versions.assertJ }" )
    testImplementation( "org.mockito:mockito-core:${ Versions.mockito }" )
}

tasks {
    withType < JavaExec > {
        standardOutput = System.`out`
    }
    withType < KotlinCompile > {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=io.ktor.util.KtorExperimentalAPI",
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
            jvmTarget = Versions.javaBytecode
        }
    }
    withType < Test > {
        useJUnitPlatform()
    }
}

deployment {
    domain = jacobs.json.JsonParser()
        .parseSettings( rootDir.resolve( "settings" ).resolve( "production-settings.json" ) )
        .socketHostname
    region = Region.EU_WEST_2
    secretsDirectory = rootProject.layout.projectDirectory.dir( "secrets" )
    server {
        hostedZoneId = "Z1ZBH69XY7O6JM"
        privateKeyName = "primary"
        securityGroupName = "StandardIO"
    }
}