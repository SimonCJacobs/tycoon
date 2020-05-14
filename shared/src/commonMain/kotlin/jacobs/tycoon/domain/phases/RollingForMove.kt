package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.StandardJailRules

open class RollingForMove (
    override val playerWithTurn: Player,
    protected val jailRules: JailRules
) : DiceRollingPhase () {

    lateinit var destinationSquare: Square
    lateinit var result: RollForMoveResult

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun getRollResult( game: Game ): RollForMoveResult {
        this.playerWithTurn.addDiceRoll( diceRoll )
        this.result = this.decideResultOfDiceRoll( game )
        return this.result
    }

    protected open fun decideResultOfDiceRoll( game: Game ): RollForMoveResult {
        return when {
            this.shouldPlayerGoToJail() -> this.sendPlayerToJail( game )
            else -> this.waitForPlayerToMove( game )
        }
    }

    private fun shouldPlayerGoToJail() : Boolean {
        return playerWithTurn.howManyDoublesRolledInRow() == jailRules.toJailDoubleCount &&
            playerWithTurn.howManyRollsSinceLastPenalisedForDoubles()
                .let { it == null || it >= jailRules.toJailDoubleCount }
    }

    private fun sendPlayerToJail( game: Game ): RollForMoveResult {
        this.destinationSquare = game.board.jailSquare
        this.playerWithTurn.penalisedForDoubleThrowing()
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = this.destinationSquare,
            outcome = RollForMoveOutcome.GO_TO_JAIL
        )
    }

    protected fun waitForPlayerToMove( game: Game ): RollForMoveResult {
        this.destinationSquare = game.board.squarePlusRoll( this.playerWithTurn.piece.square, diceRoll )
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = destinationSquare,
            outcome = RollForMoveOutcome.MOVE_TO_SQUARE
        )
    }

}