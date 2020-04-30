package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.ContentClassCollection
import jacobs.websockets.engine.ClientWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ClientWebSockets( contentClasses: ContentClassCollection = ContentClassCollection() ) {

    private val application = ClientWebSocketsApplication( contentClasses )

    suspend fun websocketClient( closure: ClientParameters.() -> Unit ): WebSocket {
        val parameters = ClientParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}