package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.SerializationLibrary
import jacobs.websockets.engine.ClientWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ClientWebSockets( serializationLibrary: SerializationLibrary = SerializationLibrary() ) {

    private val application = ClientWebSocketsApplication( serializationLibrary )

    suspend fun websocketClient( closure: ClientParameters.() -> Unit ): WebSocket {
        val parameters = ClientParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}