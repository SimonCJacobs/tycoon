package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.engine.JvmTimestampFactory
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class WebSockets {

    private val application = ServerWebSocketsApplication( JvmTimestampFactory() )

    fun closeAll() {
        this.application.closeAll()
    }

    suspend fun websocketServer( closure: ServerParameters.() -> Unit ): WebSocket {
        val parameters = ServerParameters().apply { closure() }
        return this.application.getNewWebSocket( parameters )
    }

}
