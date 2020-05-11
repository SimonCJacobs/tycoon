package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.phases.SignUp
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

    fun isGameUnderway(): Boolean {
        return this.gameState.game().isGameUnderway()
    }

    fun isSignUpPhase(): Boolean {
        return this.gameState.game().isPhase < SignUp > ()
    }

    protected fun isTurnOfOwnPlayer(): Boolean {
        val ownPlayer = gameState.game().players.getPlayerAtPosition( clientState.socket.toPosition() )
        return gameState.game().isTurnOfPlayer( ownPlayer )
    }

    fun userOfClientMachineHasSignedUpForGame(): Boolean {
        return this.gameState.game().players.hasPlayerInPositionSignedUp( clientState.socket.toPosition() )
    }

}