package jacobs.websockets

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
abstract class WebSocketParameters < T : WebSocketParameters < T > > {

    open lateinit var coroutineScope: CoroutineScope
    var notificationHandler: ( MessageContent ) -> Unit = {}
    var outgoingMessageDelay: Long = 10L
    var path: String = "/"
    var port: Int = 80
    var requestHandler: ( MessageContent ) -> MessageContent = { BooleanContent( true ) }

}