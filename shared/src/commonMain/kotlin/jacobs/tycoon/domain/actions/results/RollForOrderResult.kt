package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class RollForOrderResult (
    val diceRoll: DiceRoll,
    val nextPhase: RollForOrderResultType,
    val winner: Player = Player.NULL,
    val playersTiedFirst: Set < Player > = emptySet()
) {

    companion object {
        val NULL = RollForOrderResult( DiceRoll.NULL, RollForOrderResultType.ROLLING )
    }

}