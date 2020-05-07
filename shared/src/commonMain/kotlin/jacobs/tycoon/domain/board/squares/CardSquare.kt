package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.cards.CardSet
import kotlinx.serialization.Serializable

@Serializable
class CardSquare(
    override val indexOnBoard: Int,
    override val name: String,
    private val cardSet: CardSet
) : ActionSquare() {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}