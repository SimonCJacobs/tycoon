package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player

class PotentialRentCharge (
    override val playerWithTurn: Player,
    val occupiedProperty: Property
) : TurnBasedPhase() {

    override fun nextPhase( game: Game): GamePhase {
        TODO()
    }
}