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
import jacobs.websockets.WebSocket
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ServerWebSocketsApplication {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var container: ServerContainer
    private lateinit var server: ApplicationEngine

    private val mutex = Mutex()
    private val sockets: MutableList < WebSocket > = mutableListOf()

    fun close() {
        this.server.environment.stop()
    }

    suspend fun notifyByIndex( index: Int, notificationObject: MessageContent ) {
        this.sockets[ index ].notify( notificationObject )
    }

    fun notifyAll( notificationObject: MessageContent ) {
        this.sockets.forEach {
            eachSocket -> coroutineScope.launch { eachSocket.notify( notificationObject ) }
        }
    }

    /**
     * Note that this function will suspend indefinitely if parameters.wait = true
     */
    suspend fun startServer( parameters: ServerParameters ) {
        this.initialiseApplication( parameters )
        this.startKtorServer( parameters )
    }

    private fun initialiseApplication( parameters: ServerParameters ) {
        this.coroutineScope = parameters.coroutineScope
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
        val context = ServerContext( parameters )
        this.container.prepareForNewScope( context )
        mutex.lock() // We want to be sure we get the right socket index in the list
        this.sockets.add(
            this.container.getWebSocketInContext( context )
        )
        val socketIndex = this.sockets.size - 1
        mutex.unlock()
        parameters.newConnectionHandler.invoke( socketIndex )
        this.container.getEngineInContext( context )
            .startMessageLoop( incoming, outgoing )
    }

}