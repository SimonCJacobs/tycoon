package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.engine.ClientWebSocketsApplication
import jacobs.websockets.engine.JsTimestampFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class WebSockets {

    private val application = ClientWebSocketsApplication( JsTimestampFactory() )

    suspend fun websocketClient( closure: ClientParameters.() -> Unit ): WebSocket {
        val parameters = ClientParameters().apply { closure() }
        return application.getNewWebSocket( parameters )
    }

}