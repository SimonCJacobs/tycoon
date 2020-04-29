package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.ContentClassCollection
import jacobs.websockets.engine.JvmTimestampFactory
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ServerWebSockets ( contentClasses: ContentClassCollection? = null ) : WebSockets( contentClasses ) {

    private val application = ServerWebSocketsApplication(
        super.contentClasses, JvmTimestampFactory()
    )

    fun closeAll() {
        this.application.closeAll()
    }

    suspend fun websocketServer( closure: ServerParameters.() -> Unit ): WebSocket {
        val parameters = ServerParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}
