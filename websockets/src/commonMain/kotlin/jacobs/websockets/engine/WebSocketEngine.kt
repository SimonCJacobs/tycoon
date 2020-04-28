package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.selectUnbiased
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class WebSocketEngine( kodein: Kodein ) {

    private val coroutineScope by kodein.instance < CoroutineScope >()
    private val incomingFrameProcessor by kodein.instance < IncomingFrameProcessor > ()
    private val messageLoopDelay by kodein.instance < Long > ( tag = "outgoing" )

    private val outgoingFrameQueue: ArrayDeque < Frame > = ArrayDeque()
    private val frameProducer = coroutineScope.produce {
        while( true ) {
            if ( outgoingFrameQueue.isNotEmpty() )
                send( outgoingFrameQueue.removeFirst() )
            delay( messageLoopDelay )
        }
    }

    fun queueOutgoingFrame( frame: Frame ) {
        this.outgoingFrameQueue.addLast( frame )
    }

    suspend fun startMessageLoop( incoming: ReceiveChannel < Frame >, outgoing: SendChannel < Frame > ) {
        while( true ) {
            selectUnbiased < Any > {
                incoming.onReceive { processIncomingFrame( it ) }
                frameProducer.onReceive { outgoing.send( it ) }
            }
            delay( this.messageLoopDelay )
        }
    }

    private fun processIncomingFrame( frame: Frame ) {
        this.incomingFrameProcessor.processIncomingFrame( frame )
    }

}