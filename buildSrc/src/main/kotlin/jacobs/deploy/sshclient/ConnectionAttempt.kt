package jacobs.deploy.sshclient

import com.jcraft.jsch.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.net.ConnectException

class ConnectionAttempt(
    private val numberOfAttempts: Int,
    private val retryDelay: Long,
    private val logger: ( String ) -> Unit,
    private val sessionLambda: () -> Session
) : CoroutineScope by CoroutineScope( Dispatchers.IO ) {

    private var connectionAttempts = 0

    suspend fun tryAndConnect(): Session {
        try {
            logger( "Trying to connect over SSH" )
            connectionAttempts++
            return sessionLambda()
                .apply { connect() }
        }
        catch( e: Exception ) {
            if ( numberOfAttempts == connectionAttempts ) throw e
            logger( "SSH connection refused. ${ numberOfAttempts - connectionAttempts } attempts left" )
            delay( retryDelay )
            return tryAndConnect()
        }
    }

}