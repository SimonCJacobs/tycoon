package jacobs.tycoon.application

import jacobs.tycoon.ApplicationSettings
import jacobs.tycoon.controller.communication.CommunicationLibrary
import jacobs.tycoon.servercontroller.FrontController
import jacobs.websockets.ServerWebSockets
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class SocketListener( kodein: Kodein ) {

    private val frontController by kodein.instance < FrontController > ()

    suspend fun listenForConnections() {
        val websockets = ServerWebSockets()
        websockets.startServer {
            contentClasses = CommunicationLibrary().contentClasses
            path = ApplicationSettings.SOCKET_PATH
            port = ApplicationSettings.SOCKET_PORT
            notificationHandler = frontController::dealWithNotification
            requestHandler = frontController::dealWithRequest
        }
    }

}