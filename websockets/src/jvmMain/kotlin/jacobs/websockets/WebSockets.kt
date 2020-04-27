package jacobs.websockets

import jacobs.websockets.engine.JvmTimestampFactory
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class WebSockets {

    private val application = ServerWebSocketsApplication( JvmTimestampFactory() )

    suspend fun websocketServer( closure: ServerWebSocketParameters.() -> Unit ): WebSocket {
        val parameters = ServerWebSocketParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}
