package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class RollingForMove (
    override val playerWithTurn: Player,
    private val phasePhactory: PhasePhactory
) : DiceRolling < RollForMoveResult > () {

    lateinit var nextPhase: GamePhase

    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): RollForMoveResult {
        this.playerWithTurn.addDiceRoll( diceRoll )
        if ( this.playerWithTurn.hasRolledMaximumDoublesInARow() )
            return this.sendPlayerToJail( diceRoll )
        else
            return this.waitForPlayerToMove( game, diceRoll )
    }

    override fun nextPhase( game: Game): GamePhase {
        return this.nextPhase
    }

    private fun sendPlayerToJail( diceRoll: DiceRoll ): RollForMoveResult {
        this.nextPhase = GoToJailPhase( playerWithTurn = this.playerWithTurn )
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.GO_TO_JAIL )
    }

    private fun waitForPlayerToMove( game: Game, diceRoll: DiceRoll ): RollForMoveResult {
        this.nextPhase = this.phasePhactory.movingAPiece(
            rollingForMove = this,
            destinationSquare = game.board.squarePlusRoll( this.playerWithTurn.piece.square, diceRoll )
        )
        return RollForMoveResult( diceRoll = diceRoll, outcome = RollForMoveOutcome.MOVE_TO_SQUARE )
    }

}