package jacobs.tycoon.domain.phases.results

import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class RollForOrderResult (
    val diceRoll: DiceRoll,
    val nextPhase: RollForOrderOutcome,
    val winner: Player = Player.NULL,
    val playersTiedFirst: List < Player > = emptyList()
) {

    companion object {
        val NULL = RollForOrderResult( DiceRoll.NULL, RollForOrderOutcome.ROLLING )
    }

}