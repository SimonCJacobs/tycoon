package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class RollingForOrder private constructor(
    override val activePlayer: Player,
    private val rollResults: MutableMap < Player, DiceRoll? >,
    private val isRollOff: Boolean = false
) : DiceRolling < RollForOrderResult > () {

    companion object {
        fun firstRoll( game: Game ): RollingForOrder {
            return this.firstRollFromPlayerList( game.players.asSortedList() )
        }
        private fun firstRollFromPlayerList( players: List < Player >,
                isRollOff: Boolean = false ): RollingForOrder {
            return RollingForOrder(
                players.first(),
                players.associateWith { null }.toMutableMap(),
                isRollOff
            )
        }
    }

    // PUBLIC API

    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        this.rollResults.put( this.activePlayer, diceRoll )
        return this.continuePhaseFollowingRoll( game, diceRoll )
    }

    fun hasRollingStarted(): Boolean {
        return this.isRollOff || this.rollResults.values.filterNotNull().isNotEmpty()
    }

    override fun nullResult(): RollForOrderResult {
        return RollForOrderResult.NULL
    }

    // PRIVATE API

    private fun continuePhaseFollowingRoll( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        return when {
            false == this.hasEveryoneRolled() -> this.continueRolling( game, diceRoll )
            this.isThereASingleWinner() -> this.completePhase( game, diceRoll )
            else -> this.startARollOff( game, diceRoll )
        }
    }

    private fun continueRolling( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        game.continueRollingForThrowingOrderPhase( this.nextRollPhase() )
        return RollForOrderResult( diceRoll, RollForOrderOutcome.ROLLING )
    }

    private fun completePhase( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        val winner = this.getWinner()
        game.setWinnerOfRollForThrowingOrder( winner )
        return RollForOrderResult( diceRoll, RollForOrderOutcome.COMPLETE, winner )
    }

    private fun startARollOff( game: Game, diceRoll: DiceRoll ): RollForOrderResult {
        val rollOffPhase = this.rollOffPhase()
        game.continueRollingForThrowingOrderPhase( rollOffPhase )
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

    private fun nextRollPhase(): RollingForOrder {
        return RollingForOrder(
            this.rollResults.entries.first { it.value == null }.key,
            this.rollResults
        )
    }

    private fun rollOffPhase(): RollingForOrder {
        return firstRollFromPlayerList(
            players = this.getPlayersWithMaximumRoll().sorted(),
            isRollOff = false
        )
    }

}