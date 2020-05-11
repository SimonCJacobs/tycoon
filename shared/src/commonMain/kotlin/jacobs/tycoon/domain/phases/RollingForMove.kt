package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player

open class RollingForMove (
    override val playerWithTurn: Player
) : DiceRollingPhase () {

    lateinit var destinationSquare: Square
    lateinit var result: RollForMoveResult

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun getRollResult( game: Game ): RollForMoveResult {
        this.playerWithTurn.addDiceRoll( diceRoll )
        if ( this.playerWithTurn.hasRolledMaximumDoublesInARow() )
            this.result = this.sendPlayerToJail( game )
        else
            this.result = this.waitForPlayerToMove( game )
        return this.result
    }

    private fun sendPlayerToJail( game: Game ): RollForMoveResult {
        this.destinationSquare = game.board.jailSquare
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.GO_TO_JAIL )
    }

    private fun waitForPlayerToMove( game: Game ): RollForMoveResult {
        this.destinationSquare = game.board.squarePlusRoll( this.playerWithTurn.piece.square, diceRoll )
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.MOVE_TO_SQUARE )
    }

}