package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class RollingForMove (
    override val activePlayer: Player
) : DiceRolling < RollForMoveResult > () {

    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): RollForMoveResult {
        this.activePlayer.addDiceRoll( diceRoll )
        if ( this.activePlayer.hasRolledMaximumDoublesInARow() )
            return this.sendPlayerToJail( game, diceRoll )
        else
            return this.waitForPlayerToMove( game, diceRoll )
    }

    override fun nullResult(): RollForMoveResult {
        return RollForMoveResult.NULL
    }

    private fun sendPlayerToJail( game: Game, diceRoll: DiceRoll ): RollForMoveResult {
        game.phase = MovingAPiece(
            activePlayer = this.activePlayer,
            destinationSquare = game.board.getJailSquare()
        )
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.GO_TO_JAIL )
    }

    private fun waitForPlayerToMove( game: Game, diceRoll: DiceRoll ): RollForMoveResult {
        game.phase = MovingAPiece(
            activePlayer = this.activePlayer,
            destinationSquare = game.board.squarePlusRoll( this.activePlayer.piece.square, diceRoll )
        )
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.MOVE_TO_SQUARE )
    }

}