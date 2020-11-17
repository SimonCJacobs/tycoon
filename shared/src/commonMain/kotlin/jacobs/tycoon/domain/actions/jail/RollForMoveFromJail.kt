package jacobs.tycoon.domain.actions.jail

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.results.RollForMoveFromJailResult
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
class RollForMoveFromJail(
    override val playerPosition: SeatingPosition
) : PositionalGameAction() {

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
        val returnValue = gameController.rollTheDiceFromJailForMove( playerPosition, maybeDiceRoll )
        this.executedSuccessfully()
        return returnValue
    }

}