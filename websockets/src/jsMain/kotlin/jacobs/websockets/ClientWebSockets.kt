package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.ContentClassCollection
import jacobs.websockets.engine.ClientWebSocketsApplication
import jacobs.websockets.engine.JsTimestampFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ClientWebSockets( contentClasses: ContentClassCollection? = null ) : WebSockets( contentClasses ) {

    private val application = ClientWebSocketsApplication(
        super.contentClasses, JsTimestampFactory()
    )

    suspend fun websocketClient( closure: ClientParameters.() -> Unit ): WebSocket {
        val parameters = ClientParameters().apply { closure() }
        return application.getNewWebSocket( parameters )
    }

}