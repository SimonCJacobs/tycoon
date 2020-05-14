package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.players.Player

class Card(
    val indexInDeck: Int,
    val instruction: String,
    val action: CardAction,
    private val cardSet: CardSet
) {

    var isRetainedByPlayer: Boolean = false

    fun returnToDeck() {
        cardSet.returnToDeck( this )
    }

}