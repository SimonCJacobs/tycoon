package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.ActionSquare
import kotlinx.serialization.Serializable

@Serializable
class GoSquare(
    override val indexOnBoard: Int,
    override val name: String,
    private val creditAmount: CurrencyAmount
) : ActionSquare() {

    companion object {
        val NULL = GoSquare( 0, "", CurrencyAmount.NULL )
    }

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}