package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.Request
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class FrontController( kodein: Kodein ) {

    private val centralMainController by kodein.instance < ServerMainController > ()

    @Suppress( "RedundantSuspendModifier", "UNUSED_PARAMETER" )
    suspend fun dealWithNotification( requestObject: MessageContent) {
        // None at present (1.5.20)
    }

    suspend fun dealWithRequest( requestMessage: MessageContent): MessageContent {
        val request = requestMessage as Request
        val requestDispatcher = RequestDispatcher( request, centralMainController )
        return requestDispatcher.dispatch()
    }

}