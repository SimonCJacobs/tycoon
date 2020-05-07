package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.board.Square
import kotlinx.serialization.Serializable

@Serializable
data class MoveResult (
    val destinationSquare: Square,
    val outcome: MoveOutcome
) {

    companion object {
        val NULL = MoveResult( Square.NULL, MoveOutcome.NOTHING )
    }
}