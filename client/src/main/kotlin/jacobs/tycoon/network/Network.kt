package jacobs.tycoon.network

import org.js.mithril.SimpleRequestOptions
import org.js.mithril.request
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.FrameType
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.selectUnbiased
import kotlin.js.Promise

class Network (
    private val coroutineScope: CoroutineScope
) {

    companion object {
        private const val OUTGOING_MESSAGE_DELAY_MS = 100L
    }

    @ExperimentalStdlibApi
    private val messageQueue: ArrayDeque < String > = ArrayDeque()
    private val messageReceivers: MutableList < ( String ) -> Unit > = mutableListOf()

    @KtorExperimentalAPI
    suspend fun connect() {
        HttpClient ( Js ) {
            install( WebSockets )
        }
            .ws(
                method = HttpMethod.Get,
                host = "localhost",
                port = 8080,
                path = "/socket"
            ) {
                while ( true ) {
                    this@Network.selectIncomingOrOutgoing( incoming, outgoing )
                }
            }
    }

    fun getText() : Promise < String > {
        return request( object : SimpleRequestOptions { override val url = "http://localhost:8080/" } )
    }

    fun registerMessageReceiver( receiver: ( String ) -> Unit ) {
        this.messageReceivers.add( receiver )
    }

    @ExperimentalStdlibApi
    fun sendMessage( message: String ) {
        this.messageQueue.addLast( message )
    }

    private suspend fun selectIncomingOrOutgoing( incoming: ReceiveChannel < Frame >, outgoing: SendChannel < Frame > ) {
        selectUnbiased < Unit > {
            incoming.onReceive {
                incomingFrame -> this@Network.callMessageHandlers( incomingFrame )
            }
            getOutgoingMessageProducer().onReceive {
                outgoingMessage -> outgoing.send( Frame.Text( outgoingMessage ) )
            }
        }
    }

    @ExperimentalStdlibApi @ExperimentalCoroutinesApi
    private fun getOutgoingMessageProducer(): ReceiveChannel < String > {
        return this.coroutineScope.produce < String > {
            while ( true ) {
                if ( this@Network.messageQueue.isNotEmpty() )
                    send( this@Network.messageQueue.removeFirst() )
                delay( OUTGOING_MESSAGE_DELAY_MS )
            }
        }
    }

    private fun callMessageHandlers( messageFrame: Frame ) {
        if ( messageFrame.frameType == FrameType.TEXT ) {
            val text = ( messageFrame as Frame.Text ).readText()
            this.messageReceivers.forEach {
                eachReceiver -> eachReceiver.invoke( text )
            }
        }
    }

}