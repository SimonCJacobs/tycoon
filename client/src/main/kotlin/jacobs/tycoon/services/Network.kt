package jacobs.tycoon.services

import jacobs.tycoon.clientcontroller.IncomingController
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
    private val socketHostname by kodein.instance < String > ( tag = "socketHostname" )
    private val socketPort by kodein.instance < Int > ( tag = "socketPort" )

    private lateinit var deferredConnection: Deferred < Unit >
    private lateinit var websocket: WebSocket

    fun connect() {
        val websockets = ClientWebSockets( CommunicationLibrary().serializationLibrary )
        this.deferredConnection = this.coroutineScope.async {
            websocket = websockets
                .websocketClient {
                    coroutineScope = this@Network.coroutineScope
                    hostname = socketHostname
                    notificationHandler = { incomingController.handleNotification( it ) }
                    requestHandler = { incomingController.handleRequest( it ) }
                    port = socketPort
                }
        }
    }

    suspend fun sendRequest( request: MessageContent ): MessageContent {
        return doWhenConnected {
            websocket.request( request )
        }
    }

    private suspend fun < T > doWhenConnected( toDoLambda: suspend () -> T ): T {
        this.deferredConnection.await()
        return toDoLambda.invoke()
    }

}