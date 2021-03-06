package jacobs.tycoon.servercontroller

import jacobs.tycoon.application.ApplicationExecutorImpl
import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.domain.GameExecutor
import jacobs.websockets.SocketId
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class FrontController( kodein: Kodein ) {

    private val applicationExecutor by kodein.instance < ApplicationExecutorImpl > ()
    private val gameExecutor by kodein.instance < GameExecutor > ( tag = "wrapper" )

    @Suppress( "RedundantSuspendModifier", "UNUSED_PARAMETER" )
    suspend fun dealWithNotification( requestObject: MessageContent ) {
        // None expected at present (1.5.20)
    }

    suspend fun dealWithRequest( requestMessage: MessageContent, requestSocket: SocketId ): MessageContent {
        val request = requestMessage as Request
        val requestDispatcher = RequestDispatcher( request, requestSocket, applicationExecutor, gameExecutor )
        return requestDispatcher.dispatch()
    }

}