package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.board.cards.CardSet
import kotlinx.serialization.Serializable

@Serializable
class CardSquare(
    override val indexOnBoard: Int,
    override val name: String,
    private val cardSetName: String
) : ActionSquare() {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

    fun getNextCard( board: Board ): Card {
        return board.getNamedCardSet( cardSetName ).drawCard()
    }

}