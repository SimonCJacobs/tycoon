package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.Request
import jacobs.tycoon.domain.GameController
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class FrontController( kodein: Kodein ) {

    private val gameController by kodein.instance < GameController > ( tag = "wrapper" )

    @Suppress( "RedundantSuspendModifier", "UNUSED_PARAMETER" )
    suspend fun dealWithNotification( requestObject: MessageContent) {
        // None at present (1.5.20)
    }

    suspend fun dealWithRequest( requestMessage: MessageContent): MessageContent {
        val request = requestMessage as Request
        val requestDispatcher = RequestDispatcher( request, gameController )
        return requestDispatcher.dispatch()
    }

}