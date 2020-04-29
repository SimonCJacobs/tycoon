package jacobs.tycoon.controller.communication

import jacobs.websockets.content.MessageContent

/**
 * Don't forget that a class implementing this interface must also be annotated @Serializable
 * and registered in the CommunicationLibrary
 */
interface Request : MessageContent {
    fun < T > accept( visitor: RequestVisitor < T > ): T
}