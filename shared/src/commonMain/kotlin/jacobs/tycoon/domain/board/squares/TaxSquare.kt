package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class TaxSquare (
    override val indexOnBoard: Int,
    override val name: String,
    private val taxCharge: CurrencyAmount
) : ActionSquare() {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

    fun getTaxCharge(): CurrencyAmount {
        return this.taxCharge
    }

}