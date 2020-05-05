package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class RollForOrder : GameAction () {

    var result = RollForOrderResult.NULL

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState ) {
        this.rollForOrderGivenDiceRoll( gameState, this.result.diceRoll )
    }

    override suspend fun execute( gameState: GameState ) {
        this.result = this.rollForOrderGivenDiceRoll( gameState )
    }

    private suspend fun rollForOrderGivenDiceRoll( gameState: GameState, maybeDiceRoll: DiceRoll? = null )
            : RollForOrderResult {
        val returnValue = gameState.game().rollTheDiceForThrowingOrder( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}