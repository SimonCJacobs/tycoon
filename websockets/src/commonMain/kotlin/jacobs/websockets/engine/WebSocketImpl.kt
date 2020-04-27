package jacobs.websockets.engine

import jacobs.websockets.MessageContent
import jacobs.websockets.WebSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

// TODO: Deal with socket connection breaking!!!
// TODO: Consider and document thread safety.
@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class WebSocketImpl ( kodein: Kodein ) : WebSocket {

        // Not lazy as we need this class to initialise to complete the communication paths
    private val outgoingCommunicationDispatcher: OutgoingCommunicationDispatcher = kodein.direct.instance()

    override suspend fun close() {
        TODO()
    }

    override suspend fun notify( notificationObject: MessageContent ): MessageContent {
        return this.communicate( Notification( notificationObject ) )
    }

    override suspend fun request( requestObject: MessageContent ): MessageContent {
        return this.communicate( Request( requestObject ) )
    }

    private suspend fun communicate( communication: PrimarySocketCommunication ): MessageContent {
        return this.outgoingCommunicationDispatcher
            .dispatchPrimaryCommunication( communication )
    }

}