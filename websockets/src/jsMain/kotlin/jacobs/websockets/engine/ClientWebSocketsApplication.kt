package jacobs.websockets.engine

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientParameters
import jacobs.websockets.WebSocket
import jacobs.websockets.content.ContentClassCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientWebSocketsApplication (
    private val contentClasses: ContentClassCollection
) {
    private val client = HttpClient ( Js ) { install( WebSockets ) }
    private val container = ClientContainer( this.getCloseHandler(), this.contentClasses )
    private val detailsBySocket: MutableMap < WebSocket, SocketDetails > = mutableMapOf()

    private fun getCloseHandler(): CloseRequestHandler {
        return CloseRequestHandler { socket -> this.closeSocket( socket ) }
    }

    fun closeSocket( websocket: WebSocket ) {
        this.detailsBySocket.remove( websocket )!!
            .let {
                this.container.deleteScopeRegistry( it.context )
                it.engine.close()
            }
    }

    suspend fun getNewWebSocket( parameters: ClientParameters ): WebSocket {
        val context = ClientContext( parameters )
        val engine = this.container.getEngineInContext( context )
        val socket = this.container.getWebSocketInContext( context )
        this.container.prepareForNewScope( context )
        this.startEngine( engine, parameters )
        this.retainSocketHandle( socket, context, engine )
        return socket
    }

    private suspend fun startEngine( engine: WebSocketEngine, parameters: ClientParameters ) {
        parameters.coroutineScope.launch { launchEngine( engine, parameters ) }
    }

    private suspend fun launchEngine( engine: WebSocketEngine, parameters: ClientParameters  ) {
        this.client.ws(
            method = HttpMethod.Get,
            host = parameters.hostname,
            port = parameters.port,
            path = parameters.path
        ) {
            engine.startMessageLoop( incoming, outgoing )
        }
    }

    private fun retainSocketHandle( socket: WebSocket, context: ClientContext, engine: WebSocketEngine ) {
        this.detailsBySocket.put( socket, SocketDetails( context, engine ) )
    }

    data class SocketDetails(
        val context: ClientContext,
        val engine: WebSocketEngine
    )

}