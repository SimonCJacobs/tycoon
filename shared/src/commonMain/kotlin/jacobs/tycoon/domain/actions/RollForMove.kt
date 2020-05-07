package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class RollForMove : GameAction () {

    var result = RollForMoveResult.NULL

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState ) {
        this.rollForMoveGivenDiceRoll( gameState, this.result.diceRoll )
    }

    override suspend fun execute( gameState: GameState ) {
        this.result = this.rollForMoveGivenDiceRoll( gameState )
    }

    private suspend fun rollForMoveGivenDiceRoll( gameState: GameState, maybeDiceRoll: DiceRoll? = null )
            : RollForMoveResult {
        val returnValue = gameState.game().rollTheDiceForMove( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}