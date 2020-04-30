package jacobs.websockets.engine

import io.ktor.application.install
import io.ktor.http.cio.websocket.Frame
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ServerWebSocketsApplication {

    private lateinit var container: ServerContainer
    private lateinit var server: ApplicationEngine
    private var socketIndex: Int = 0

    fun close() {
        this.server.environment.stop()
    }

    /**
     * Note that this function will suspend indefinitely if parameters.wait = true
     */
    suspend fun startServer( parameters: ServerParameters ) {
        this.initialiseApplication( parameters )
        this.startKtorServer( parameters )
    }

    private fun initialiseApplication( parameters: ServerParameters ) {
        this.container = ServerContainer(
            closeRequestHandler = CloseRequestHandler { throw Error( "Not implemented" ) },
            parameters = parameters
        )
    }

    private suspend fun startKtorServer( parameters: ServerParameters ) {
        this.server = embeddedServer( Netty, parameters.port ) {
            install( WebSockets )
            routing {
                webSocket( parameters.path ) {
                    makeNewSocketConnection( parameters, incoming, outgoing )
                }
            }
        }
        server.start( wait = parameters.wait )
    }

    private suspend fun makeNewSocketConnection( parameters: ServerParameters,
             incoming: ReceiveChannel < Frame >, outgoing: SendChannel < Frame > ) {
        val context = ServerContext( this.socketIndex++, parameters = parameters )
        this.container.prepareForNewScope( context )
        this.container.getEngineInContext( context )
            .startMessageLoop( incoming, outgoing )
    }

}