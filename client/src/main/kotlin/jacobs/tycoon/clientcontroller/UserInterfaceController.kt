package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

open class UserInterfaceController( kodein: Kodein ) : CoroutineScope by kodein.direct.instance() {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()

    fun isClientWaitingForServer(): Boolean {
        return this.clientState.isWaitingForServer
    }

    fun hasGameBeenInitialised(): Boolean {
        return this.gameState.hasGame()
    }

    fun isSignUpPhase(): Boolean {
        return this.gameState.game().isSignUpPhase()
    }

    fun ownPlayerIsInTheGame(): Boolean {
        return this.gameState.game().players.hasPlayerInPosition( clientState.socket.toPosition() )
    }

}