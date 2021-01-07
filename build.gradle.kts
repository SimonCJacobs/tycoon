import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

repositories {
    jcenter()
}

plugins {
    kotlin( "multiplatform" ) apply false
    kotlin( "plugin.serialization" ) version Versions.kotlin apply false
    id( "jacobs-private-repo-plugin" ) version Versions.jacobsPrivateRepoPlugin apply false
}

gradle.addBuildListener(
    object : BuildAdapter() {
        override fun buildFinished( result: BuildResult ) {
            val formatter = DateTimeFormatter.ofPattern( "HH:mm.ss" )
            println( "Finished build at ${ formatter.format( LocalDateTime.now() ) }")
        }
    }
)