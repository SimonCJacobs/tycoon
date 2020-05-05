package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class DiceController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameState by kodein.instance < GameState > ()

    fun firstDie(): Int {
        return this.gameState.game().dice.lastRoll.first
    }

    fun secondDie(): Int {
        return this.gameState.game().dice.lastRoll.second
    }

    fun shouldDiceBeShown(): Boolean {
        if ( this.gameState.game().isSignUpPhase() )
            return false
        val phase = this.gameState.game().phase
        if ( phase is RollingForOrder && false == phase.hasRollingStarted() )
            return false
        return true
    }

}