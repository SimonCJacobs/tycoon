package jacobs.deploy.tasks

import jacobs.deploy.aws.Ec2
import jacobs.deploy.aws.Ec2SshClient
import jacobs.deploy.aws.PrivateKey
import jacobs.deploy.aws.Route53
import jacobs.deploy.configuration.ServerConfiguration
import jacobs.deploy.sshclient.LoginName
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.gradle.api.file.RegularFile
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.ApplicationPluginConvention
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.application.CreateStartScripts
import org.gradle.api.tasks.bundling.Tar
import org.gradle.kotlin.dsl.property
import org.gradle.util.TextUtil
import java.nio.file.Path

open class DeployServer : Deploy() {

    companion object {
        private const val CLOSE_CHECK_DELAY = 500L
    }

    @Input
    val applicationExecutableName: Provider < String > =
        project.providers.provider {
            project.tasks.withType( CreateStartScripts::class.java )
                .getByName( ApplicationPlugin.TASK_START_SCRIPTS_NAME ).applicationName
        }

    @Input
    val executablesDirectoryName: Provider < String > = getApplicationPluginConventionProperty { executableDir }

    @Input @Suppress( "UnstableApiUsage" )
    val privateKey: Property < PrivateKey > = project.objects.property < PrivateKey > ()
        .convention(
            project.provider {
                PrivateKey(
                    name = serverConfiguration.privateKeyName,
                    directory = deployConfiguration.get().secretsDirectory.asFile
                )
            } )

    @Input
    val tarTask: TaskProvider < Tar > =
        this.project.tasks.named( ApplicationPlugin.TASK_DIST_TAR_NAME, Tar::class.java  )

    @InputFile
    val tarFile: Provider < RegularFile > = tarTask.map { it.archiveFile.get() }

    private var maybeEc2: Ec2? = null
    private var maybeEc2SshClient: Ec2SshClient? = null
    private var maybeRoute53: Route53? = null

    private val ec2: Ec2 get() = maybeEc2!!
    private val ec2SshClient: Ec2SshClient get() = maybeEc2SshClient!!
    private val route53: Route53 get() = maybeRoute53!!

    private val serverConfiguration: ServerConfiguration
        get() = deployConfiguration.get().serverConfiguration!!

    private fun < T > getApplicationPluginConventionProperty( property: ApplicationPluginConvention.() -> T ): Provider < T > {
        return project.providers.provider {
            project.convention.getPlugin( ApplicationPluginConvention::class.java )
                .property()
        }
    }

    override fun description(): String {
        return "Deploys the client code to AWS EC2 and starts the application"
    }

    @TaskAction
    override fun deploy() {
        runBlocking {
            try {
                deployServerUnwrapped()
            }
            finally {
                shutdown()
            }
        }
    }

    private suspend fun deployServerUnwrapped() {
        joinAll(
            launch { launchEc2Instance() },
            launch { connectToRoute53Service() }
        )
        joinAll (
            launch { startApplicationOverSsh() },
            launch { associateEc2IpWithDomain() }
        )
        waitForExitCommand()
    }

    private suspend fun launchEc2Instance() {
        this.maybeEc2 = Ec2 (
            mainConfig = deployConfiguration.get(),
            serverConfiguration = serverConfiguration,
            sdkCredentials = this.sdkCredentials(),
            privateKey = this.privateKey.get()
        )
        this.ec2.connect()
        log( "Connected to EC2 service. Booting instance...")
        this.ec2.runInstance()
        log( "EC2 instance running" )
    }

    @Suppress( "RedundantSuspendModifier" )
    private suspend fun connectToRoute53Service() {
        this.maybeRoute53 = Route53(
            hostedZoneId = serverConfiguration.hostedZoneId,
            sdkCredentials = this.sdkCredentials()
        )
        this.route53.connect()
        log( "Connected to Route 53 service" )
    }

    private suspend fun startApplicationOverSsh() {
        this.maybeEc2SshClient = Ec2SshClient(
            loginName = LoginName(
                this.serverConfiguration.userName,
                this.ec2.getIpAddress()
            ),
            privateKey = this.privateKey.get(),
            tarFile = tarFile.get().asFile,
            executablePathInTar = this.getExecutablePathOfApplicationWithinTar( tarTask.get() ),
            logger = { log( it ) }
        )
        this.ec2SshClient.startApplication()
    }

    private fun getExecutablePathOfApplicationWithinTar( tarTask: Tar ): Path {
        return Path.of(
            ".",
            TextUtil.minus( tarTask.archiveFileName.get(), "." + tarTask.archiveExtension.get() ),
            executablesDirectoryName.get(),
            applicationExecutableName.get()
        )
    }

    private suspend fun associateEc2IpWithDomain() {
        this.associateIpWithDomain( this.ec2.getIpAddress() )
    }

    private suspend fun associateIpWithDomain( ipAddress: String ) {
        route53.associateIpWithDomain(
            domain = deployConfiguration.get().domain,
            ipAddress = ipAddress
        )
        log( "IP address $ipAddress associated with domain" )
    }

    private suspend fun waitForExitCommand() {
        while ( readLine() != "exit" ) { delay( CLOSE_CHECK_DELAY ) }
        log ( "Exit command received" )
    }

    private suspend fun shutdown() {
        this.closeResources( maybeEc2SshClient, maybeEc2, maybeRoute53 )
    }

}