package jacobs.tycoon.domain.actions.moving

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.results.RollForOrderResult
import jacobs.tycoon.domain.dice.DiceRoll
import kotlinx.serialization.Serializable

@Serializable
class RollForOrderAction : GameAction() {

    var result = RollForOrderResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.rollForOrderGivenDiceRoll( gameController, this.result.diceRoll )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = this.rollForOrderGivenDiceRoll( gameController )
    }

    private fun rollForOrderGivenDiceRoll( gameController: GameController,
              maybeDiceRoll: DiceRoll? = null ): RollForOrderResult {
        val returnValue = gameController.rollTheDiceForThrowingOrder( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}