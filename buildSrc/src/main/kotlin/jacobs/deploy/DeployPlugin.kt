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

abstract class DeployPlugin : Plugin < Project > {

    companion object {
        const val DEPLOY_TASK_NAME = "deploy"
    }

    protected lateinit var project: Project

    override fun apply( target: Project ) {
        this.project = target
        target.extensions.create < DeployPluginConfiguration > ( "deployment", target )
        this.registerTasks()
    }

    protected abstract fun registerTasks()

}