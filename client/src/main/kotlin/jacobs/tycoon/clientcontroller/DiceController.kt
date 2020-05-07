package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class DiceController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()

    fun firstDie(): Int {
        return this.gameState.game().getLastRoll().first
    }

    fun secondDie(): Int {
        return this.gameState.game().getLastRoll().second
    }

    fun shouldDiceBeShown(): Boolean {
        return this.gameState.game().haveDiceBeenRolled()
    }

}