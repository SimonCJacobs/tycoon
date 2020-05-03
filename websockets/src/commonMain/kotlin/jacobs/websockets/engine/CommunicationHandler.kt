package jacobs.websockets.engine

import jacobs.websockets.NotificationHandler
import jacobs.websockets.RequestHandler
import jacobs.websockets.RequestHandlerWithIndex
import jacobs.websockets.SocketId
import jacobs.websockets.WebSocketParameters
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalStdlibApi @ExperimentalCoroutinesApi
class CommunicationHandler(
    parameters: WebSocketParameters < * >,
    private val socket: SocketId
) {
    private val notificationHandler: NotificationHandler? = parameters.notificationHandler
    private val requestHandler: RequestHandler? = parameters.requestHandler
    private val requestHandlerWithIndex: RequestHandlerWithIndex? = parameters.requestHandlerWithIndex

    suspend fun notify( content: MessageContent ) {
        this.notificationHandler?.invoke( content )
    }

    suspend fun request( content: MessageContent ): MessageContent {
        return when {
            null != this.requestHandlerWithIndex -> this.requestHandlerWithIndex.invoke( content, this.socket )
            null != this.requestHandler -> this.requestHandler.invoke( content )
            else -> BooleanContent( false )
        }
    }

}