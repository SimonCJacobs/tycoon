package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import jacobs.websockets.BooleanContent
import jacobs.websockets.MessageContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * Unique for each socket because knows destination of incoming notifications and requests
 */
@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class IncomingFrameProcessor( kodein: Kodein ) : CommunicationVisitor {

    private val communicationCodec by kodein.instance < CommunicationCodec > ()
    private val responseDirector by kodein.instance < ResponseDirector > ()
    private val jsonSerializer by kodein.instance < JsonSerializer > ()
    private val notificationHandler by kodein.instance < ( MessageContent ) -> Unit > ( tag = "notification" )
    private val requestHandler by kodein.instance < ( MessageContent ) -> MessageContent > ( tag = "request" )

    fun processIncomingFrame( frame: Frame ) {
        this.jsonSerializer.frameToMessage( frame )
            .also { dispatchMessage( it ) }
    }

    private fun dispatchMessage( message: Message ) {
        this.communicationCodec.getCommunicationFromIncomingMessage( message )
            .accept( this )
    }

        // Visitor methods processing incoming SocketCommunication instances

    override fun visit( notification: Notification ) {
        this.notificationHandler.invoke( notification.content )
        this.responseDirector.dispatchOutgoingResponse(
            notification.getResponseWithNewContent( this.getConfirmationContent() )
        )
    }

    override fun visit( request: Request ) {
        val newContent = this.requestHandler.invoke( request.content )
        this.responseDirector.dispatchOutgoingResponse(
            request.getResponseWithNewContent( newContent )
        )
    }

    override fun visit( response: Response ) {
        this.responseDirector.dispatchIncomingResponse( response )
    }

    private fun getConfirmationContent(): BooleanContent {
        return BooleanContent( true )
    }

}