package jacobs.tycoon.domain.actions.jail

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.results.RollForMoveFromJailResult
import kotlinx.serialization.Serializable

@Serializable
class RollForMoveFromJail : GameAction() {

    var result: RollForMoveFromJailResult = RollForMoveFromJailResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.rollForMoveFromJailGivenDiceRoll( gameController, this.result.diceRoll )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = this.rollForMoveFromJailGivenDiceRoll( gameController )
    }

    private fun rollForMoveFromJailGivenDiceRoll( gameController: GameController,
                  maybeDiceRoll: DiceRoll? = null ) : RollForMoveFromJailResult {
        val returnValue = gameController.rollTheDiceFromJailForMove( actorPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}