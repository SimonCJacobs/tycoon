package jacobs.deploy.aws

import jacobs.deploy.resources.AutoCloseableSuspend
import jacobs.deploy.sshclient.PromptedCommand
import jacobs.deploy.sshclient.LoginName
import jacobs.deploy.sshclient.SshClient
import java.io.File
import java.nio.file.Path

class Ec2SshClient(
    loginName: LoginName,
    privateKey: PrivateKey,
    private val tarFile: File,
    private val executablePathInTar: Path,
    private val logger: ( String ) -> Unit
) : AutoCloseableSuspend {

    companion object {
        private const val EC2_PROMPT_SUFFIX = "~]\$"
    }

    private val sshClient = SshClient( loginName, privateKey.file, logger )

    suspend fun startApplication() {
        this.loginToInstance()
        this.installJava11()
        this.uploadAndUnzipTar()
        this.startUnzippedApplication()
    }

    private suspend fun loginToInstance() {
            // Necessary to disable host key check as it's to a brand new IP
            // so we wouldn't have it stored.
            // TODO: In theory, should do something to verify fingerprint though to ensure no MITM attack.
        this.sshClient.connect( disableHostKeyCheck = true )
        // TODO check logged in
        logger( "Logged into instance over SSH" )
    }

    private fun installJava11() {
        logger( "Installing Java 11 on instance" )
        this.sshClient.sendPromptedCommandsAndWaitForPrompt(
            EC2_PROMPT_SUFFIX,
            PromptedCommand( EC2_PROMPT_SUFFIX, "sudo amazon-linux-extras install java-openjdk11" ),
            PromptedCommand( "Is this ok [y/d/N]:", "y" )
        )
        // TODO check installed successfully
        logger( "Successfully installed Java 11" )
    }

    private fun uploadAndUnzipTar() {
        logger( "Uploading application" )
        this.sshClient.uploadFile( tarFile )
        this.sshClient.sendCommandAndWaitForPrompt( "tar -xf ${ tarFile.name }", EC2_PROMPT_SUFFIX ) // eXtract, Filename
        // TODO check extracted successfully
        logger( "Tar uploaded and unzipped" )
    }

    private fun startUnzippedApplication() {
        logger( "Starting application" )
        this.sshClient.sendOpenCommand( executablePathInTar.toString() )
        logger( "Application is running" )
    }

    override suspend fun close() {
        this.sshClient.close()
    }

}