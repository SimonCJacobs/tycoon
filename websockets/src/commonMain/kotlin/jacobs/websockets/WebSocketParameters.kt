package jacobs.websockets

import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
abstract class WebSocketParameters < T : WebSocketParameters < T > > {

    open lateinit var coroutineScope: CoroutineScope
    var notificationHandler: ( MessageContent ) -> Unit = {}
    var outgoingMessageDelay: Long = 10L
    var path: String = "/"
    var port: Int = 80
    var requestHandler: ( MessageContent ) -> MessageContent = { BooleanContent( true ) }

}