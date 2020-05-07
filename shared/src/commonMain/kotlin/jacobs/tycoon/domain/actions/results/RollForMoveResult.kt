package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.dice.DiceRoll
import kotlinx.serialization.Serializable

@Serializable
data class RollForMoveResult (
    val diceRoll: DiceRoll,
    val outcome: RollForMoveOutcome
) {

    companion object {
        val NULL = RollForMoveResult( DiceRoll.NULL, RollForMoveOutcome.MOVE_TO_SQUARE )
    }

}