package jacobs.deploy.sshclient

import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.Session
import java.io.InputStream
import java.io.OutputStream

class ShellSession (
    private val session: Session,
    private val promptedCommands: List < PromptedCommand >,
    private val finalPrompt: String
) {

    companion object {
        private const val BUFFER_SIZE = 1024
        private const val SHELL_CHANNEL_TYPE = "shell"
    }

    fun connect() {
        val channel = this.session.openChannel( SHELL_CHANNEL_TYPE ) as ChannelShell
        channel.connect()
        val inStream = channel.inputStream
        val outStream = channel.outputStream
        promptedCommands.forEach {
            this.waitForPrompt( it.prompt, inStream )
            this.writeToStream( it.command + "\r", outStream )
        }
        this.waitForPrompt( finalPrompt, inStream )
        channel.disconnect()
    }

    private fun writeToStream( command: String, outputStream: OutputStream) {
        outputStream.write( command.toByteArray() )
        outputStream.flush()
    }

    private fun waitForPrompt( prompt: String, inputStream: InputStream) {
        val readResult = StringBuilder()
        while ( readResult.contains( prompt ) == false ) {
            val tmp = ByteArray( BUFFER_SIZE )
            inputStream.read( tmp )
            readResult.append( String( tmp ) )
        }
    }

}