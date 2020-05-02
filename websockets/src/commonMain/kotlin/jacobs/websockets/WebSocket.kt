package jacobs.websockets

import jacobs.websockets.content.MessageContent

interface WebSocket {
    fun close()
    suspend fun notify( notificationObject: MessageContent ): MessageContent
    suspend fun request( requestObject: MessageContent): MessageContent
}