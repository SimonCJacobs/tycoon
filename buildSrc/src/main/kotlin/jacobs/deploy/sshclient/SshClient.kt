package jacobs.deploy.sshclient

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import jacobs.deploy.resources.AutoCloseableSuspend
import kotlinx.coroutines.delay
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class SshClient (
    private val loginName: LoginName,
    private val privateKeyFile: File,
    private val logger: ( String ) -> Unit
) : AutoCloseableSuspend {

    companion object {
        private const val EXEC_CHANNEL_TYPE = "exec"
        private const val RETRY_ATTEMPTS = 10
        private const val RETRY_DELAY = 2000L
        private const val SFTP_CHANNEL_TYPE = "sftp"
    }

    private val jsch = JSch()
        .apply { addIdentity( privateKeyFile.absolutePath ) }
    private lateinit var session: Session

    suspend fun connect( disableHostKeyCheck: Boolean = false ) {
        this.session = ConnectionAttempt( RETRY_ATTEMPTS, RETRY_DELAY, logger ) { newSession( disableHostKeyCheck) }
            .tryAndConnect()
    }

    private fun newSession( disableHostKeyCheck: Boolean ): Session {
        return this.jsch.getSession( loginName.username, loginName.hostname )
            .apply { if ( disableHostKeyCheck ) setConfig( "StrictHostKeyChecking", "no" ) }
    }

    fun sendCommandAndWaitForPrompt( command: String, prompt: String ) {
        this.shellFromPromptAndCommands( prompt, command )
            .connect()
    }

    fun sendPromptedCommandsAndWaitForPrompt( closingPrompt: String, vararg promptsAndCommands: PromptedCommand ) {
        ShellSession( this.session, promptsAndCommands.toList(), closingPrompt )
            .connect()
    }

    fun sendOpenCommand( command: String ) {
        val channel = this.session.openChannel( EXEC_CHANNEL_TYPE ) as ChannelExec
        channel.setCommand( command + "\n" )
        channel.connect()
    }

    fun uploadFile( file: File ) {
        val channel = this.session.openChannel( SFTP_CHANNEL_TYPE ) as ChannelSftp
        channel.connect()
        channel.put( file.absolutePath, file.name )
        channel.disconnect()
    }

    override suspend fun close() {
        this.session.disconnect()
    }

    private fun shellFromPromptAndCommands( prompt: String, vararg commands: String ): ShellSession {
        return ShellSession( this.session, commands.map { PromptedCommand( prompt, it ) }, prompt )
    }

}