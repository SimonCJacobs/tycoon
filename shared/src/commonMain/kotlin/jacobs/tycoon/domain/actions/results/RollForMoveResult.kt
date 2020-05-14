package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import kotlinx.serialization.Serializable

@Serializable
data class RollForMoveResult (
    val diceRoll: DiceRoll,
    val destinationSquare: Square,
    val outcome: RollForMoveOutcome
) {

    companion object {
        val NULL = ofRollOnly( DiceRoll.NULL )

        fun ofRollOnly( diceRoll: DiceRoll ): RollForMoveResult {
            return RollForMoveResult( diceRoll, Square.NULL, RollForMoveOutcome.MOVE_TO_SQUARE  )
        }
    }

}