package jacobs.deploy.configuration

import software.amazon.awssdk.services.ec2.model.InstanceType

class ServerConfiguration {

    /**
     * The Route 53 hosted zone id
     */
    lateinit var hostedZoneId: String
    var imageId: String = "ami-032598fcc7e9d1c7a"
    var instanceType: InstanceType = InstanceType.T2_NANO
    lateinit var privateKeyName: String
    lateinit var securityGroupName: String
    var userName: String = "ec2-user"

}