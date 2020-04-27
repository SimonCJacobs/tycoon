package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.engine.JvmTimestampFactory
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI @ImplicitReflectionSerializer
class WebSockets {

    private val application = ServerWebSocketsApplication( JvmTimestampFactory() )

    suspend fun websocketServer( closure: ServerWebSocketParameters.() -> Unit ): WebSocket {
        val parameters = ServerWebSocketParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}
