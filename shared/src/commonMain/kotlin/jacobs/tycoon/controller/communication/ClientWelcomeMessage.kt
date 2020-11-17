package jacobs.tycoon.controller.communication

import jacobs.websockets.SocketId
import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
class ClientWelcomeMessage(
    val message: String
) : MessageContent