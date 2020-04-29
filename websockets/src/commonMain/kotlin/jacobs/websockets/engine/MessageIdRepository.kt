package jacobs.websockets.engine

import jacobs.websockets.content.MessageContent

internal class MessageIdRepository {

    private val unansweredMessages: MutableMap < MessageIdentifier, (MessageContent) -> Unit > = mutableMapOf()

    fun logOutgoingMessage( message: Message, responseCallback: (MessageContent) -> Unit ) {
        this.unansweredMessages.put( message.id, responseCallback )
    }

    fun isRecognisedResponse( response: Response ): Boolean {
        return this.unansweredMessages.keys.any {
            it.matches( response.id!! )
        }
    }

    fun getResponseRouteToOrigin( response: Response ): (MessageContent) -> Unit {
        return this.unansweredMessages.keys.first { it.matches( response.id!! ) }
            .let { key -> this.unansweredMessages.getValue( key ) }
    }

}

