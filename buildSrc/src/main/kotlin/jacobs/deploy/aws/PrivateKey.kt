package jacobs.deploy.aws

import java.io.File

class PrivateKey (
    val name: String,
    directory: File
) {

    companion object {
        const val PRIVATE_KEY_EXTENSION = "pem"
    }

    val file: File = directory.toPath().resolve( "$name.$PRIVATE_KEY_EXTENSION" ).toFile()

}