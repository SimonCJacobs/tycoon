package jacobs.deploy.aws

import jacobs.deploy.configuration.DeployPluginConfiguration
import jacobs.deploy.configuration.ServerConfiguration
import jacobs.deploy.resources.AutoCloseableSuspend
import kotlinx.coroutines.delay
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.ec2.model.Instance
import software.amazon.awssdk.services.ec2.model.InstanceStateName
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse
import software.amazon.awssdk.services.ec2.model.TerminateInstancesRequest

class Ec2 (
    private val mainConfig: DeployPluginConfiguration,
    private val serverConfiguration: ServerConfiguration,
    private val privateKey: PrivateKey,
    private val sdkCredentials: SdkCredentials
) : AutoCloseableSuspend {

    companion object {
        private const val INITIAL_INSTANCE_CHECK_DELAY = 2000L
        private const val INSTANCE_CHECK_DELAY = 2000L
    }

    private lateinit var ec2: Ec2Client

    private lateinit var runInstancesResponse: RunInstancesResponse

    fun connect() {
        this.ec2 = Ec2Client.builder()
            .region( mainConfig.region )
            .credentialsProvider( sdkCredentials )
            .build()
    }

    suspend fun runInstance() {
        val runInstancesRequest = RunInstancesRequest.builder()
            .instanceType( this.serverConfiguration.instanceType )
            .imageId( this.serverConfiguration.imageId )
            .keyName( this.privateKey.name )
            .minCount( 1 )
            .maxCount( 1 )
            .securityGroups( this.serverConfiguration.securityGroupName )
            .build()
        this.runInstancesResponse = this.ec2.runInstances( runInstancesRequest ) //.await()
        this.waitForInstanceToBeRunning()
    }

    fun getIpAddress(): String {
        return this.getInstance()
            .publicIpAddress()
    }

    override suspend fun close() {
        this.ec2.terminateInstances(
            TerminateInstancesRequest.builder()
                .instanceIds( this.getInstanceId() )
                .build()
        )
        this.ec2.close()
    }

    private fun getInstanceId(): String {
        return this.runInstancesResponse
            .instances()
            .single()
            .instanceId()
    }

    private suspend fun waitForInstanceToBeRunning() {
        delay( INITIAL_INSTANCE_CHECK_DELAY )
        while ( this.isInstanceRunning() == false ) {
            delay( INSTANCE_CHECK_DELAY  )
        }
    }

    private fun isInstanceRunning(): Boolean {
        val state = this.getInstance().state().name()
        return state == InstanceStateName.RUNNING
    }

    private fun getInstance(): Instance {
        return this.ec2.describeInstances()
            .reservations()
            .flatMap { it.instances() }
            .first { it.instanceId() == this.getInstanceId() }
    }

}