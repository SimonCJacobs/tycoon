package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.players.Player

class MovingAPiece (
    override val activePlayer: Player,
    val destinationSquare: Square
) : SinglePlayerPhase() {

    fun isSquareDropTarget( square: Square ): Boolean {
        return this.destinationSquare == square
    }

}