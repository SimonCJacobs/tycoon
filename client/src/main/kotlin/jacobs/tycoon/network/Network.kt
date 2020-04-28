package jacobs.tycoon.network

import jacobs.tycoon.ApplicationSettings
import jacobs.websockets.MessageContent
import jacobs.websockets.StringContent
import jacobs.websockets.WebSocket
import jacobs.websockets.WebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Network ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val messageReceivers: MutableList < ( String ) -> Unit > = mutableListOf()

    private lateinit var deferredConnection: Deferred < Unit >
    private lateinit var websocket: WebSocket

    fun connect() {
        this.deferredConnection = this.coroutineScope.async {
            websocket = WebSockets()
                .websocketClient {
                    coroutineScope = this@Network.coroutineScope
                    hostname = ApplicationSettings.SOCKET_HOST
                    notificationHandler = { handleNotification( it ) }
                    requestHandler = { handleRequest( it ) }
                    port = ApplicationSettings.SOCKET_PORT
                }
        }
    }

    fun handleNotification( messageContent: MessageContent ) {
        console.log( "network received notification", messageContent )
    }

    fun handleRequest( messageContent: MessageContent ): StringContent {
        console.log( "network received request", messageContent )
        return StringContent( "my beard is green" )
    }

    fun registerMessageReceiver( receiver: ( String ) -> Unit ) {
        this.messageReceivers.add( receiver )
    }

    fun sendStringRequest( str: String, doAfter: ( String ) -> Unit ) {
        coroutineScope.launch {
            deferredConnection.await()
            val returnObject = websocket.request( StringContent( str ) )
            val returnContent = ( returnObject as StringContent ).string
            doAfter( returnContent )
        }
    }

}