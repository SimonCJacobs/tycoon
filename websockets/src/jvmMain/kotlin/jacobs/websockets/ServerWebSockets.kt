package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.MessageContent
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ServerWebSockets {

    private val application = ServerWebSocketsApplication()

    fun close() {
        this.application.close()
    }

    fun notifyAll( notificationObject: MessageContent ) {
        this.application.notifyAll( notificationObject )
    }

    suspend fun notifySocket( socket: SocketId, notificationObject: MessageContent ) {
        this.application.notifySocket( socket, notificationObject )
    }

    suspend fun startServer( closure: ServerParameters.() -> Unit ) {
        val parameters = ServerParameters().apply { closure() }
        return this.application.startServer( parameters )
    }

}
