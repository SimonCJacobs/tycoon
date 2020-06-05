package jacobs.deploy

import jacobs.deploy.configuration.DeployPluginConfiguration
import jacobs.deploy.tasks.DeployClient
import jacobs.deploy.tasks.DeployServer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create

class DeployServerPlugin : DeployPlugin() {

    companion object {
        const val FAT_JAR_TASK_NAME = "fatJar"
    }

    override fun registerTasks() {
        this.registerDeployServerTask()
        this.registerFatJarTask()
    }

    @Suppress( "UnstableApiUsage" )
    private fun registerDeployServerTask() {
        project.tasks.register( DEPLOY_TASK_NAME, DeployServer::class.java ) {
            this.deployConfiguration.set(
                project.extensions.getByType( DeployPluginConfiguration::class.java )
            )
        }
    }

    @Suppress( "UnstableApiUsage" )
    private fun registerFatJarTask() {
        project.tasks.register( FAT_JAR_TASK_NAME, Jar::class.java ) {
            group = "Build"
            description = "Assembles a fat jar containing all dependencies, suitable for independent execution."
            archiveClassifier.set( "fat" )
            manifest {
                attributes.put(
                    "Main-Class",
                    project.extensions.findByType( JavaApplication::class.java )!!.mainClassName
                )
            }
            from (
                project.convention.getPlugin( JavaPluginConvention::class.java )
                    .sourceSets.getByName( SourceSet.MAIN_SOURCE_SET_NAME )
                    .runtimeClasspath
                    .mapNotNull {
                        if ( it.exists() )
                            if ( it.isDirectory() ) it else project.zipTree( it )
                        else
                            null
                    }
            )
        }
    }

}