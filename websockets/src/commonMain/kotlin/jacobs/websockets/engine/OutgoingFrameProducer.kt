package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class OutgoingFrameProducer( kodein: Kodein) {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val messageLoopDelay by kodein.instance < Long > ( tag = "outgoing" )

    private val outgoingFrameQueue: ArrayDeque < Frame > = ArrayDeque()

    val producer = coroutineScope.produce {
        while ( true ) {
            if ( outgoingFrameQueue.isNotEmpty() )
                send( outgoingFrameQueue.removeFirst() )
            delay( messageLoopDelay )
        }
    }

    fun queueOutgoingFrame( frame: Frame ) {
        this.outgoingFrameQueue.addLast( frame )
    }

}