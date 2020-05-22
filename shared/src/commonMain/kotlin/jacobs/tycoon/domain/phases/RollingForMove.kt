package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.results.RollForMoveOutcome
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules

open class RollingForMove (
    playerWithTurn: Player,
    private val jailRules: JailRules
) : RollingForMoveBase( playerWithTurn ) {

    var result: RollForMoveResult = RollForMoveResult.NULL

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun getRollResult( game: Game ): RollForMoveResult {
        this.playerWithTurn.addDiceRoll( diceRoll )
        this.result = this.decideResultOfDiceRoll( game )
        return this.result
    }

    private fun decideResultOfDiceRoll( game: Game ): RollForMoveResult {
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
        this.playerWithTurn.penalisedForDoubleThrowing()
        return RollForMoveResult(
            diceRoll = diceRoll,
            destinationSquare = game.board.jailSquare,
            outcome = RollForMoveOutcome.GO_TO_JAIL
        )
    }

}