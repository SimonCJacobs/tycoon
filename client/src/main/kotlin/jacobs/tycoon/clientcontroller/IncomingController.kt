package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.ClientWelcomeMessage
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.websockets.content.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class IncomingController ( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val stateSynchroniser by kodein.instance < StateSynchroniser > ()

    suspend fun handleNotification( messageContent: MessageContent ) {
            // Lazy switch..... but hm not expecting any more of these
        when ( messageContent) {
            is GameActionCollection -> stateSynchroniser.applyUpdates( messageContent )
            is ClientWelcomeMessage -> this.readWelcomeMessage( messageContent )
            else -> throw Error( "No other notification types expected" )
        }
    }

    @Suppress( "UNUSED_PARAMETER" )
    fun handleRequest( messageContent: MessageContent ): MessageContent {
        throw Error( "Not expecting any requests" )
    }

    private fun readWelcomeMessage( welcomeMessage: ClientWelcomeMessage ) {
        console.log( welcomeMessage.message )
    }

}