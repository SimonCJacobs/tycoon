package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.results.RollForOrderResult
import jacobs.tycoon.domain.phases.results.RollForOrderOutcome
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class RollingForOrder (
    override val playerWithTurn: Player,
    val rollResults: MutableMap < Player, DiceRoll? >
) : DiceRollingPhase() {

    lateinit var result: RollForOrderResult

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun noteDiceRoll( game: Game ): RollForOrderResult {
        this.rollResults.put( this.playerWithTurn, this.diceRoll )
        this.figureOutResult( game )
        return this.result
    }

    // PRIVATE API

    private fun figureOutResult( game: Game ) {
        when {
            false == this.hasEveryoneRolled() -> this.continueRolling()
            this.isThereASingleWinner() -> this.completePhase( game )
            else -> this.startARollOff()
        }
    }

    private fun continueRolling() {
        this.result = RollForOrderResult( diceRoll, RollForOrderOutcome.ROLLING )
    }

    private fun completePhase( game: Game ) {
        val winner = this.getWinner()
        game.moveAllPiecesToStartingSquare()
        this.result = RollForOrderResult( diceRoll, RollForOrderOutcome.COMPLETE, winner )
    }

    private fun startARollOff() {
        this.result = RollForOrderResult(
            diceRoll = diceRoll,
            nextPhase = RollForOrderOutcome.ROLL_OFF,
            playersTiedFirst = this.getPlayersWithMaximumRoll()
        )
    }

    private fun getPlayersWithMaximumRoll(): List < Player > {
        return this.rollResults.values.filterNotNull().map { it.result }.maxOrNull()
            .let { maxValue -> this.rollResults.filterValues { it?.result == maxValue }.keys }
            .toList()
    }

    private fun getWinner(): Player {
        return this.getPlayersWithMaximumRoll().single()
    }

    private fun isThereASingleWinner(): Boolean {
        return this.getPlayersWithMaximumRoll().size == 1
    }

    private fun hasEveryoneRolled(): Boolean {
        return false == this.rollResults.containsValue( null )
    }

}