package jacobs.tycoon.servercontroller

import jacobs.tycoon.application.ApplicationExecutorImpl
import jacobs.tycoon.controller.communication.BinaryGameActionRequest
import jacobs.tycoon.controller.communication.GameActionRequest
import jacobs.tycoon.controller.communication.GameActionRequestWithResponse
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.controller.communication.RequestVisitor
import jacobs.tycoon.controller.communication.application.ApplicationRequest
import jacobs.tycoon.domain.GameExecutor
import jacobs.websockets.SocketId
import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.MessageContent

internal class RequestDispatcher(
    private val request: Request,
    private val socketId: SocketId,
    private val applicationExecutor: ApplicationExecutorImpl,
    private val gameExecutor: GameExecutor
) : RequestVisitor < MessageContent > {

    suspend fun dispatch(): MessageContent {
        return this.request.accept( this )
    }

    override suspend fun visit( applicationRequest: ApplicationRequest ): MessageContent {
        return applicationRequest.action.run {
            this.socketId = this@RequestDispatcher.socketId
            applicationExecutor.execute( this )
            BooleanContent( this.successful )
        }
    }

    override suspend fun visit( gameActionRequest: BinaryGameActionRequest ): MessageContent {
        this.doGameActionRequest( gameActionRequest )
        return BooleanContent( gameActionRequest.action.successful )
    }

    override suspend fun visit( gameActionRequest: GameActionRequestWithResponse ): MessageContent {
        this.doGameActionRequest( gameActionRequest )
        return gameActionRequest.action.getResponse().toMessageContent()
    }

    private suspend fun doGameActionRequest( gameActionRequest: GameActionRequest ) {
        gameExecutor.execute( gameActionRequest.action )
    }

}