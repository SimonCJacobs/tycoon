package jacobs.tycoon.application

import jacobs.tycoon.controller.communication.CommunicationLibrary
import jacobs.tycoon.servercontroller.FrontController
import jacobs.websockets.NewConnectionHandler
import jacobs.websockets.ServerWebSockets
import jacobs.websockets.SocketId
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class SocketServer ( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope >()
    private val frontController by kodein.instance < FrontController > ()
    private val socketPath by kodein.instance < String > ( tag = "socketPath" )
    private val socketPort by kodein.instance < Int > ( tag = "socketPort" )
    private lateinit var newConnectionLambda: NewConnectionHandler
    private lateinit var websockets: ServerWebSockets

    fun setNewConnectionLambda( lambda: NewConnectionHandler ) {
        this.newConnectionLambda = lambda
    }

    suspend fun startServer() {
        this.websockets = ServerWebSockets()
        websockets.startServer {
            newConnectionHandler = newConnectionLambda
            notificationHandler = { frontController.dealWithNotification( it ) }        
            path = socketPath
            port = socketPort
            requestHandlerWithIndex =
                { message, socket -> frontController.dealWithRequest( message, socket ) }
            serializationLibrary = CommunicationLibrary().serializationLibrary
            wait = false
        }
    }

    fun notifySocketAtIndex( socket: SocketId, notification: MessageContent  ) {
        coroutineScope.launch { websockets.notifySocket( socket, notification ) }
    }

}