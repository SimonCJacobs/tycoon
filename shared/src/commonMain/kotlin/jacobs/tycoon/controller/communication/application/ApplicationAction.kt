package jacobs.tycoon.controller.communication.application

import jacobs.websockets.SocketId
import kotlinx.serialization.Serializable

@Serializable
abstract class ApplicationAction {

    abstract val requiresAuthorisation: Boolean
    var socketId: SocketId = SocketId.NULL
    var successful = false

    abstract suspend fun execute( applicationExecutor: ApplicationExecutor )

}