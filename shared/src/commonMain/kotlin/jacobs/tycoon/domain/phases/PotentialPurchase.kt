package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.Player

class PotentialPurchase (
    override val playerWithTurn: Player,
    val targetProperty: Property
) : TurnBasedPhase() {

    override fun nextPhase( game: Game): GamePhase {
        TODO()
    }

}