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

class DeployClientPlugin : DeployPlugin() {

    override fun registerTasks() {
        project.tasks.register( DEPLOY_TASK_NAME, DeployClient::class.java ) {
            this.deployConfiguration.set(
                project.extensions.getByType( DeployPluginConfiguration::class.java )
            )
        }
    }

}