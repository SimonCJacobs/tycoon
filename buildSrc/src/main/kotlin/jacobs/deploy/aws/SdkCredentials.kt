package jacobs.deploy.aws

import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import java.io.File
import java.io.FileInputStream
import java.util.*

class SdkCredentials( secretsDirectory: File ) : AwsCredentials, AwsCredentialsProvider {

    companion object {
        const val AWS_PROPERTIES_FILE = "aws.properties"
        const val ACCESS_KEY_PROPERTY = "accessKeyId"
        const val SECRET_KEY_PROPERTY = "secretAccessKey"
    }

    private val awsProperties = Properties()

    init {
        val awsPropertiesFile = secretsDirectory.toPath().resolve( AWS_PROPERTIES_FILE )
        val fileStream = FileInputStream( awsPropertiesFile.toFile() )
        this.awsProperties.load( fileStream )
    }

    override fun accessKeyId(): String {
        return awsProperties.getProperty( ACCESS_KEY_PROPERTY )
    }

    override fun secretAccessKey(): String {
        return awsProperties.getProperty( SECRET_KEY_PROPERTY )
    }

    override fun resolveCredentials(): AwsCredentials {
        return this
    }

}