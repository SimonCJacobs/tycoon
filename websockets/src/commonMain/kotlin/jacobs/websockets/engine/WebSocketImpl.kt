package jacobs.websockets.engine

import jacobs.websockets.content.MessageContent
import jacobs.websockets.WebSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

// TODO: Deal with socket connection breaking!!!
// TODO: Consider and document thread safety.
@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class WebSocketImpl ( kodein: Kodein ) : WebSocket {

    private val closeSocketLambda by kodein.instance < ( WebSocket ) -> Unit >( tag = "closeSocket" )
    private val outgoingCommunicationDispatcher by kodein.instance < OutgoingCommunicationDispatcher > ()

    override fun close() {
        this.closeSocketLambda.invoke( this )
    }

    override suspend fun notify( notificationObject: MessageContent): MessageContent {
        return this.communicate( Notification( notificationObject ) )
    }

    override suspend fun request( requestObject: MessageContent): MessageContent {
        return this.communicate( Request( requestObject ) )
    }

    private suspend fun communicate( communication: PrimarySocketCommunication ): MessageContent {
        return this.outgoingCommunicationDispatcher
            .dispatchPrimaryCommunication( communication )
    }

}