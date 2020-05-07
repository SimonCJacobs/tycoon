package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.players.Player

class GoToJailPhase (
    override val playerWithTurn: Player
) : TurnBasedPhase() {
    override fun nextPhase( game: Game): GamePhase {
        TODO()
    }
}