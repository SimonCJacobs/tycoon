package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.AddPlayerRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.RequestVisitor
import jacobs.tycoon.controller.communication.SimpleRequest
import jacobs.tycoon.controller.communication.SimpleRequestWrapper
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent

internal class RequestDispatcher(
    private val request: Request,
    private val serverMainController: ServerMainController
) : RequestVisitor < MessageContent > {

    suspend fun dispatch(): MessageContent {
        return this.request.accept( this )
    }

    override suspend fun visit( simpleRequestWrapper: SimpleRequestWrapper ): MessageContent {
        return when ( simpleRequestWrapper.identifier ) {
            SimpleRequest.START_GAME -> this.serverMainController.startGame().let { BooleanContent( it ) }
        }
    }

    override suspend fun visit( addPlayerRequest: AddPlayerRequest ): MessageContent {
        return this.serverMainController.addPlayer( addPlayerRequest.name, addPlayerRequest.piece )
            .let { BooleanContent( it ) }
    }

}