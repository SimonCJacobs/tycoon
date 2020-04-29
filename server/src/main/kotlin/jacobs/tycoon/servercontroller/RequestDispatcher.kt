package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.RequestVisitor
import jacobs.tycoon.controller.communication.SimpleRequest
import jacobs.tycoon.controller.communication.SimpleRequestWrapper
import jacobs.websockets.content.MessageContent

internal class RequestDispatcher(
    private val request: Request,
    private val serverMainController: ServerMainController
) : RequestVisitor <MessageContent> {

    fun dispatch(): MessageContent {
        return this.request.accept( this )
    }

    override fun visit( simpleRequestWrapper: SimpleRequestWrapper ): MessageContent {
        return when( simpleRequestWrapper.identifier ) {
            SimpleRequest.AVAILABLE_PIECES -> serverMainController.getAvailablePieces()
        }
    }

}