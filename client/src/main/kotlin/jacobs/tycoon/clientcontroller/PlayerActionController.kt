package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerActionController(
    kodein: Kodein,
    private val player: Player
): UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun isItOwnTurn(): Boolean {
        return gameState.game().isTurnOfPlayer( this.player )
    }

    fun rollTheDice() {
        launch { outgoingRequestController.rollTheDice() }
    }

}