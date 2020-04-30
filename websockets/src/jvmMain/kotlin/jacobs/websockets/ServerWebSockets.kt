package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.ContentClassCollection
import jacobs.websockets.engine.JvmTimestampFactory
import jacobs.websockets.engine.ServerWebSocketsApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ServerWebSockets {

    private val application = ServerWebSocketsApplication()

    fun close() {
        this.application.close()
    }

    suspend fun startServer( closure: ServerParameters.() -> Unit ) {
        val parameters = ServerParameters().apply { closure() }
        return this.application.startServer( parameters )
    }

}
