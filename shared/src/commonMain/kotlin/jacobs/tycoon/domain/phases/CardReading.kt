package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.results.ReadCardResult
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.players.Player

class CardReading (
    override val playerWithTurn: Player,
    private val cardSquare: CardSquare
) : TurnBasedPhase {

    val card: Card = this.cardSquare.getNextCard()
    val result: ReadCardResult = ReadCardResult( card.instruction )

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}