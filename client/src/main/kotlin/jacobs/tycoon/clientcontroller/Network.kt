package jacobs.tycoon.clientcontroller

import jacobs.tycoon.ApplicationSettings
import jacobs.tycoon.controller.communication.CommunicationLibrary
import jacobs.websockets.content.MessageContent
import jacobs.websockets.WebSocket
import jacobs.websockets.ClientWebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Network ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val incomingController by kodein.instance < IncomingController > ()

    private lateinit var deferredConnection: Deferred < Unit >
    private lateinit var websocket: WebSocket

    fun connect() {
        val websockets = ClientWebSockets( CommunicationLibrary().contentClasses )
        this.deferredConnection = this.coroutineScope.async {
            websocket = websockets
                .websocketClient {
                    coroutineScope = this@Network.coroutineScope
                    hostname = ApplicationSettings.SOCKET_HOST
                    notificationHandler = { incomingController.handleNotification( it ) }
                    requestHandler = { incomingController.handleRequest( it ) }
                    port = ApplicationSettings.SOCKET_PORT
                }
        }
    }

    fun sendRequestAsync( request: MessageContent ): Deferred < MessageContent > {
        return doWhenConnectedAsync {
            websocket.request( request )
        }
    }

    private suspend fun < T > doWhenConnected( toDoLambda: suspend () -> T ): T {
        this.deferredConnection.await()
        return toDoLambda.invoke()
    }

    private fun < T > doWhenConnectedAsync( toDoLambda: suspend () -> T ): Deferred < T > {
        return this.coroutineScope.async {
            deferredConnection.await()
            toDoLambda.invoke()
        }
    }

}