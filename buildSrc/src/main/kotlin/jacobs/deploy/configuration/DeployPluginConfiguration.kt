package jacobs.deploy.configuration

import org.gradle.api.Project
import org.gradle.api.file.Directory
import software.amazon.awssdk.regions.Region

open class DeployPluginConfiguration (
    project: Project
) {

    companion object {
        const val SECRETS_DIRECTORY = "secrets"
    }

    internal var clientConfiguration: ClientConfiguration? = null
    internal var serverConfiguration: ServerConfiguration? = null

    lateinit var domain: String
    var region: Region = Region.EU_WEST_2
    var secretsDirectory: Directory = project.layout.projectDirectory.dir( SECRETS_DIRECTORY )

    fun client( closure: ( ClientConfiguration.() -> Unit )? = null ) {
        clientConfiguration = ClientConfiguration()
            .apply { closure?.invoke( this ) }
    }

    fun server( closure: ServerConfiguration.() -> Unit ) {
        serverConfiguration = ServerConfiguration()
            .apply { closure() }
    }

}