package jacobs.websockets

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
abstract class WebSocketParameters < T : WebSocketParameters < T > > {

    open lateinit var coroutineScope: CoroutineScope
    var notificationHandler: NotificationHandler? = null
    var outgoingMessageDelay: Long = 10L
    var path: String = "/"
    var port: Int = 80
    var requestHandler: RequestHandler? = null

    /**
     * Provide a request handler that provides the index of the requesting socket. This will be
     * executed in preference to any [requestHandler] registered
     */
    var requestHandlerWithIndex: RequestHandlerWithIndex? = null

}