package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
abstract class PositionalGameAction (
) : GameAction() {
    abstract val playerPosition: SeatingPosition
}