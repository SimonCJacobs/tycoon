package jacobs.websockets

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
abstract class WebSocketParameters {

    lateinit var coroutineScope: CoroutineScope
    /**
     * The frequency with which the outgoing frame queue is checked before passing to outgoing channel
     */
    var notificationHandler: ( MessageContent ) -> Unit = {}
    var outgoingMessageDelay: Long = 10L
    var requestHandler: ( MessageContent ) -> MessageContent = { BooleanContent( true ) }
    var path: String = "/"
    var port: Int = 80

    internal fun asKodeinModule(): Kodein.Module  {
        return Kodein.Module( "parameters" ) {
            bind < CoroutineScope >() with instance( coroutineScope )
            bind < ( MessageContent ) -> Unit >( tag = "notification" ) with
                instance( notificationHandler )
            bind < ( MessageContent ) -> MessageContent > ( tag = "request" ) with
                instance( requestHandler )
            bind < Long >( tag = "outgoing" ) with instance( outgoingMessageDelay )
        }
    }
}