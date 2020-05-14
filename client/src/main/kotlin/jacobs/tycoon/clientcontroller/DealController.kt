package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class DealController (kodein: Kodein ) {

    private val clientState by kodein.instance <ClientState> ()
    private val gameState by kodein.instance < GameState > ()

    fun cancelComposingDeal() {
        this.clientState.isPlayerComposingDeal = false
    }

}