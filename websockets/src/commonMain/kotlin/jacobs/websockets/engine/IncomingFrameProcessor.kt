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

    private val communicationCodec: CommunicationCodec by kodein.instance()
    private val responseDirector: ResponseDirector by kodein.instance()
    private val jsonSerializer: JsonSerializer by kodein.instance()
    private val notificationHandler: ( MessageContent ) -> Unit by kodein.instance( tag = "notification" )
    private val requestHandler: ( MessageContent ) -> MessageContent by kodein.instance( tag = "request" )

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