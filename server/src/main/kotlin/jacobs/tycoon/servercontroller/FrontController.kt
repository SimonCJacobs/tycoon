package jacobs.tycoon.servercontroller

import jacobs.tycoon.controller.communication.Request
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class FrontController( kodein: Kodein ) {

    private val centralMainController by kodein.instance < ServerMainController > ()

    fun dealWithNotification( requestObject: MessageContent) {

    }

    fun dealWithRequest( requestMessage: MessageContent): MessageContent {
        val request = requestMessage as Request
        val requestDispatcher = RequestDispatcher( request, centralMainController )
        return requestDispatcher.dispatch()
    }

}