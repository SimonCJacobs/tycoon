package jacobs.tycoon.application

import jacobs.tycoon.ApplicationSettings
import jacobs.tycoon.controller.communication.CommunicationLibrary
import jacobs.tycoon.servercontroller.FrontController
import jacobs.websockets.ServerWebSockets
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class SocketServer ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope >()
    private val frontController by kodein.instance < FrontController > ()
    private lateinit var newConnectionLambda: ( Int ) -> Unit
    private lateinit var websockets: ServerWebSockets

    fun setNewConnectionLambda( lambda: ( Int ) -> Unit ) {
        this.newConnectionLambda = lambda
    }

    suspend fun startServer() {
        this.websockets = ServerWebSockets()
        websockets.startServer {
            serializationLibrary = CommunicationLibrary().serializationLibrary
            path = ApplicationSettings.SOCKET_PATH
            port = ApplicationSettings.SOCKET_PORT
            newConnectionHandler = newConnectionLambda
            notificationHandler = { frontController.dealWithNotification( it ) }
            requestHandler = { frontController.dealWithRequest( it ) }
            wait = false
        }
    }

    fun notifySocketOfIndex( index: Int, notification: MessageContent  ) {
        coroutineScope.launch { websockets.notifyByIndex( index, notification ) }
    }

    fun notifyAllSockets( notification: MessageContent ) {
        this.websockets.notifyAll( notification )
    }

}