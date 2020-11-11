package jacobs.tycoon.controller.communication.application

import jacobs.tycoon.domain.GameExecutor
import jacobs.websockets.SocketId

interface ApplicationExecutor {
    val gameExecutor: GameExecutor
    suspend fun execute( applicationAction: ApplicationAction )
    fun requestAuthorisation( username: String, socketId: SocketId ): Boolean
}