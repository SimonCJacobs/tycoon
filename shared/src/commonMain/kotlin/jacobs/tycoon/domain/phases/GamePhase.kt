package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class GamePhase {
    override fun equals( other: Any? ): Boolean {
        if ( other == null )
            return false
        else
            return this::class == other::class
    }

    override fun hashCode(): Int {
        return this::class.simpleName.hashCode()
    }
}

@Serializable
object SignUp : GamePhase()

@Serializable
sealed class SinglePlayerPhase: GamePhase() {
    protected abstract val activePlayer: Player
    fun isTurnOfPlayer( testPlayer: Player ): Boolean { return this.activePlayer == testPlayer }
}

sealed class DiceRollPhase : SinglePlayerPhase() {
    abstract fun actOnRoll( game: Game, diceRoll: DiceRoll ): Boolean
}

@Serializable
class RollForMove ( override val activePlayer: Player ) : DiceRollPhase() {
    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): Boolean {
        TODO()
    }
}

@Serializable
class RollForThrowingOrder private constructor(
    override val activePlayer: Player,
    private val rollResults: MutableMap < Player, DiceRoll > = mutableMapOf()
): DiceRollPhase() {

    companion object {
        fun firstRoll( game: Game): RollForThrowingOrder {
            return RollForThrowingOrder(  game.players.first() )
        }
    }

    fun hasRollingStarted(): Boolean {
        return this.rollResults.isNotEmpty()
    }

    override fun actOnRoll( game: Game, diceRoll: DiceRoll ): Boolean {
        this.rollResults.put( this.activePlayer, diceRoll )
        if ( this.hasEveryoneRolled( game ) )
            return game.startGameProperGivenDiceRolls( this.rollResults )
        else
            return game.continueRollingForThrowingOrder( this.nextRollPhase( game ) )
    }

    private fun hasEveryoneRolled( game: Game ): Boolean {
        return game.players.count() == this.rollResults.size
    }

    private fun nextRollPhase( game: Game ): RollForThrowingOrder {
        return RollForThrowingOrder(
            game.players.next( this.activePlayer ),
            this.rollResults
        )
    }

}