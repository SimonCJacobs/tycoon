package jacobs.deploy.tasks

import jacobs.deploy.aws.SdkCredentials
import jacobs.deploy.configuration.DeployPluginConfiguration
import jacobs.deploy.resources.AutoCloseableSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class Deploy : DefaultTask() {

    @Input
    val deployConfiguration: Property < DeployPluginConfiguration > =
        project.objects.property( DeployPluginConfiguration::class.java )

    init {
        this.registerBasicProperties()
    }

    private fun registerBasicProperties() {
        group = "Distribution"
        description = description()
    }

    protected abstract fun description(): String

    @TaskAction
    abstract fun deploy()

    protected suspend fun closeResources( vararg resources: AutoCloseableSuspend? ) {
        resources.map { launch { it?.close() } }
            .joinAll()
    }

    protected fun launch( block: suspend CoroutineScope.() -> Unit ): Job {
        return CoroutineScope( Dispatchers.IO ).launch { block() }
    }

    protected fun log( message: String ) {
        project.logger.quiet( message )
    }

    protected fun sdkCredentials(): SdkCredentials {
        return SdkCredentials( deployConfiguration.get().secretsDirectory.asFile )
    }

}