package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.phases.results.ReadCardResult
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.players.Player

class CardReading (
    override val playerWithTurn: Player,
    private val cardProvider: () -> Card
) : TurnBasedPhase {

    lateinit var card: Card
    lateinit var result: ReadCardResult

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun readCard(): ReadCardResult {
        this.card = this.cardProvider()
        this.result = ReadCardResult( this.card.instruction )
        return this.result
    }

}