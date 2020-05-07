package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class Utility(
    override val indexOnBoard: Int,
    override val name: String,
    override val listPrice: CurrencyAmount
) : Property() {

    override fun < T > accept( squareVisitor: SquareVisitor<T>): T {
        return squareVisitor.visit( this )
    }

    override fun rent(): Int {
        TODO("Not yet implemengted")
    }

}