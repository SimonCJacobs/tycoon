package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.board.squares.Square
import kotlinx.serialization.Serializable

@Serializable
data class MoveResult (
    val destinationSquare: Square,
    val outcome: MoveOutcome,
    val didPassGo: Boolean
) {

    companion object {
        val NULL = MoveResult( Square.NULL, MoveOutcome.FREE_PARKING, false )
    }

}