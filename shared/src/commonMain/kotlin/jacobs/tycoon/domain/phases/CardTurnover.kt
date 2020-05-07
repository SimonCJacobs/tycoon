package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.players.Player

class CardTurnover (
    override val playerWithTurn: Player,
    private val cardSquare: CardSquare
) : TurnBasedPhase() {

    override fun nextPhase( game: Game): GamePhase {
        TODO()
    }

}