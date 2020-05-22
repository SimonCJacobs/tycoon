package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.results.RollForMoveOutcome
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.players.Player

abstract class RollingForMoveBase(
    override val playerWithTurn: Player
) : DiceRollingPhase() {

    protected fun waitForPlayerToMove( game: Game ): RollForMoveResult {
        val destinationSquare = game.board.squarePlusRoll( this.playerWithTurn.piece.square, diceRoll )
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = destinationSquare,
            outcome = RollForMoveOutcome.MOVE_TO_SQUARE
        )
    }

}