package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
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

    override suspend fun duplicate( gameController: GameController ) {
        this.rollForOrderGivenDiceRoll( gameController, this.result.diceRoll )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = this.rollForOrderGivenDiceRoll( gameController )
    }

    private suspend fun rollForOrderGivenDiceRoll(gameController: GameController,
                                                  maybeDiceRoll: DiceRoll? = null )
            : RollForOrderResult {
        val returnValue = gameController.game().rollTheDiceForThrowingOrder( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}