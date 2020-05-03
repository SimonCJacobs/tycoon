package jacobs.tycoon.clientcontroller

import jacobs.mithril.Mithril
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class IncomingController ( kodein: Kodein ) {

    private val mithril = Mithril()
    private val stateSynchroniser by kodein.instance < StateSynchroniser > ()

    fun handleNotification( messageContent: MessageContent ) {
            // State updates are only notification at time of writing (1.5.20)
        val updates = messageContent as GameActionCollection
        stateSynchroniser.applyUpdates( updates )
        mithril.redraw()
    }

    @Suppress( "UNUSED_PARAMETER" )
    fun handleRequest( messageContent: MessageContent ): MessageContent {
        throw Error( "Not expecting any requests" )
    }

}