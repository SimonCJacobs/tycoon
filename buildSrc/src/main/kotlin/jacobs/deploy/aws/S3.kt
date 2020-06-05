package jacobs.deploy.aws

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

class S3 (
    private val bucket: String,
    private val region: Region,
    private val sdkCredentials: SdkCredentials
) {

    private lateinit var s3: S3Client

    fun connect() {
        this.s3 = S3Client.builder()
            .region( region )
            .credentialsProvider( sdkCredentials )
            .build()
    }

    fun uploadFile( filePath: Path ) {
        this.s3.putObject(
            PutObjectRequest.builder()
                .bucket( bucket )
                .key( filePath.fileName.toString() )
                .build(),
            filePath
        )
    }

}