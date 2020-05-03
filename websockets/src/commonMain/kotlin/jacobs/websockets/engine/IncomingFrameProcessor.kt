package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * Unique for each socket because knows destination of incoming notifications and requests
 */
@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class IncomingFrameProcessor( kodein: Kodein ) : CommunicationVisitor {

    private val coroutineScope by kodein.instance < CoroutineScope >()
    private val communicationCodec by kodein.instance < CommunicationCodec > ()
    private val communicationHandler by kodein.instance < CommunicationHandler > ()
    private val responseDirector by kodein.instance < ResponseDirector > ()
    private val jsonSerializer by kodein.instance < JsonSerializer > ()

    // TODO a close request from the other party will come through here and
    fun processIncomingFrame( frame: Frame ) {
        this.jsonSerializer.frameToMessage( frame )
            .also { dispatchMessage( it ) }
    }

    private fun dispatchMessage( message: Message ) {
        val communication = this.communicationCodec.getCommunicationFromIncomingMessage( message )
        this.coroutineScope.launch { communication.accept( this@IncomingFrameProcessor ) }
    }

        // Visitor methods processing incoming SocketCommunication instances

    override suspend fun visit( notification: Notification ) {
        this.communicationHandler.notify( notification.content )
        this.responseDirector.dispatchOutgoingResponse(
            notification.getResponseWithNewContent( this.getConfirmationContent() )
        )
    }

    override suspend fun visit( request: Request ) {
        val newContent = this.communicationHandler.request( request.content )
        this.responseDirector.dispatchOutgoingResponse(
            request.getResponseWithNewContent( newContent )
        )
    }

    override suspend fun visit( response: Response ) {
        this.responseDirector.dispatchIncomingResponse( response )
    }

    private fun getConfirmationContent(): BooleanContent {
        return BooleanContent(true)
    }

}