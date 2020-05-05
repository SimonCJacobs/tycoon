package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.OpenActionRequest
import jacobs.tycoon.controller.communication.PositionalActionRequest
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.RequestVisitor
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.GameExecutor
import jacobs.websockets.SocketId
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent

internal class RequestDispatcher(
    private val request: Request,
    private val socketId: SocketId,
    private val gameExecutor: GameExecutor
) : RequestVisitor < MessageContent > {

    suspend fun dispatch(): MessageContent {
        return this.request.accept( this )
    }

    override suspend fun visit(openActionRequest: OpenActionRequest ): MessageContent {
        return this.gameExecutor.execute( openActionRequest.action )
            .let { BooleanContent( it.successful ) }
    }

    override suspend fun visit( positionalActionRequest: PositionalActionRequest ): MessageContent {
        return positionalActionRequest.positionalAction.run {
            position = socketId.toPosition()
            gameExecutor.execute( this )
                .let { BooleanContent( it.successful ) }
        }
    }

}