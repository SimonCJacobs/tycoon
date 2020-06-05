package jacobs.deploy.aws

import jacobs.deploy.resources.AutoCloseableSuspend
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asDeferred
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.route53.Route53AsyncClient
import software.amazon.awssdk.services.route53.model.Change
import software.amazon.awssdk.services.route53.model.ChangeAction
import software.amazon.awssdk.services.route53.model.ChangeBatch
import software.amazon.awssdk.services.route53.model.ChangeResourceRecordSetsRequest
import software.amazon.awssdk.services.route53.model.RRType
import software.amazon.awssdk.services.route53.model.ResourceRecord
import software.amazon.awssdk.services.route53.model.ResourceRecordSet

class Route53(
    private val hostedZoneId: String,
    private val sdkCredentials: SdkCredentials
) : AutoCloseableSuspend {

    private lateinit var client: Route53AsyncClient

    private val modifiedDomainsWithIpAddresses: MutableMap < String, String > = mutableMapOf()

    @Suppress( "RedundantSuspendModifier" )
    suspend fun connect() {
        this.client = Route53AsyncClient.builder()
            .credentialsProvider( sdkCredentials )
            .region( Region.AWS_GLOBAL )
            .build()
    }

    suspend fun associateIpWithDomain( domain: String, ipAddress: String ) {
        this.modifyDomainIpAddress( domain, ipAddress, ChangeAction.UPSERT ).join()
        this.modifiedDomainsWithIpAddresses.put( domain, ipAddress)
    }

    override suspend fun close() {
        this.modifiedDomainsWithIpAddresses.map {
            this.modifyDomainIpAddress( it.key, it.value, ChangeAction.DELETE )
        }
            .forEach { it.join() }
        this.client.close()
    }

    private fun modifyDomainIpAddress( domain: String, ipAddress: String, action: ChangeAction ): Job {
        return this.client.changeResourceRecordSets(
            ChangeResourceRecordSetsRequest.builder()
                .changeBatch(
                    ChangeBatch.builder()
                        .changes(
                            Change.builder()
                                .action( action )
                                .resourceRecordSet(
                                    ResourceRecordSet.builder()
                                        .name( domain )
                                        .resourceRecords(
                                            ResourceRecord.builder()
                                                .value( ipAddress )
                                                .build()
                                        )
                                        .ttl( 30L )
                                        .type( RRType.A )
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .hostedZoneId( this.hostedZoneId )
                .build()
        ).asDeferred()
    }

}