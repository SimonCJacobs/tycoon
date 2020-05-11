package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.dice.DiceRoll

abstract class DiceRollingPhase : TurnBasedPhase {

    protected lateinit var diceRoll: DiceRoll
        private set

    fun setDiceRoll( diceRoll: DiceRoll ) {
        this.diceRoll = diceRoll
    }

}