package jacobs.tycoon.domain.actions.moving

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.dice.DiceRoll
import kotlinx.serialization.Serializable

@Serializable
class RollForMove : GameAction() {

    var result = RollForMoveResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.rollForMoveGivenDiceRoll( gameController, this.result.diceRoll )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = this.rollForMoveGivenDiceRoll( gameController )
    }

    private fun rollForMoveGivenDiceRoll( gameController: GameController, maybeDiceRoll: DiceRoll? = null )
            : RollForMoveResult {
        val returnValue = gameController.rollTheDiceForMove( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}