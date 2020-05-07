package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class RollingForOrder (
    override val playerWithTurn: Player,
    private val rollResults: MutableMap < Player, DiceRoll? >,
    private val phasePhactory: PhasePhactory
) : DiceRolling < RollForOrderResult > () {

    private lateinit var nextPhase: GamePhase

    // PUBLIC API

    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        this.rollResults.put( this.playerWithTurn, diceRoll )
        return this.figureOutNextPhaseAndGetResult( game, diceRoll )
    }

    override fun nextPhase( game: Game ): GamePhase {
        return this.nextPhase
    }

    // PRIVATE API

    private fun figureOutNextPhaseAndGetResult( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        return when {
            false == this.hasEveryoneRolled() -> this.continueRolling( diceRoll )
            this.isThereASingleWinner() -> this.completePhase( game, diceRoll )
            else -> this.startARollOff( diceRoll )
        }
    }

    private fun continueRolling( diceRoll: DiceRoll ): RollForOrderResult {
        this.nextPhase = this.phasePhactory.continueRollingForOrder( this.rollResults )
        return RollForOrderResult( diceRoll, RollForOrderOutcome.ROLLING )
    }

    private fun completePhase( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        val winner = this.getWinner()
        game.moveAllPiecesToStartingSquare()
        this.nextPhase = this.phasePhactory.rollingForMove( winner )
        return RollForOrderResult( diceRoll, RollForOrderOutcome.COMPLETE, winner )
    }

    private fun startARollOff( diceRoll: DiceRoll ): RollForOrderResult {
        val rollOffPhase = this.phasePhactory.rollOffAmongstPlayers( this.getPlayersWithMaximumRoll() )
        this.nextPhase = rollOffPhase
        return RollForOrderResult(
            diceRoll = diceRoll,
            nextPhase = RollForOrderOutcome.ROLL_OFF,
            playersTiedFirst = rollOffPhase.rollResults.keys
        )
    }

    private fun getPlayersWithMaximumRoll(): Set < Player > {
        return this.rollResults.values.filterNotNull().map { it.result }.max()
            .let { maxValue -> this.rollResults.filterValues { it?.result == maxValue }.keys }
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