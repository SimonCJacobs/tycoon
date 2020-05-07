package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.dice.DiceRoll

abstract class DiceRolling < TResult > : SinglePlayerPhase() {

    abstract fun actOnRoll( game: Game, diceRoll: DiceRoll ): TResult
    abstract fun nullResult(): TResult

}