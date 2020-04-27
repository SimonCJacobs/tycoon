package jacobs.websockets.engine

import jacobs.websockets.MessageContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class OutgoingCommunicationDispatcher ( kodein: Kodein ) {

    private val communicationCodec: CommunicationCodec by kodein.instance()
    private val jsonSerializer: JsonSerializer by kodein.instance()
    private val messageIdRepository: MessageIdRepository by kodein.instance()
    private val responseDirector: ResponseDirector by kodein.instance()
    private val webSocketEngine: WebSocketEngine by kodein.instance()

    init {
        this.responseDirector.setOutgoingResponsePath { response -> dispatchResponse( response ) }
    }

    suspend fun dispatchPrimaryCommunication( communication: PrimarySocketCommunication ): MessageContent {
        return suspendCoroutine { continuation ->
            this.dispatchCommunicationAndCallback( communication, continuation )
        }
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun dispatchResponse( response: Response ) {
        this.communicationCodec.getMessageFromResponse( response )
            .also { this.passMessageToEngine( it ) }
    }

    private fun dispatchCommunicationAndCallback( communication: PrimarySocketCommunication,
             continuation: Continuation < MessageContent > ) {
        val message = this.communicationCodec.getMessageFromPrimaryCommunication( communication )
        this.messageIdRepository.logOutgoingMessage( message, continuation.resumeCallback() )
        this.passMessageToEngine( message )
    }

    private fun passMessageToEngine( message: Message ) {
        this.webSocketEngine.queueOutgoingFrame(
            this.jsonSerializer.messageToFrame( message )
        )
    }

    private fun < T > Continuation < T >.resumeCallback(): ( T ) -> Unit {
        return { this.resume( it ) }
    }

}