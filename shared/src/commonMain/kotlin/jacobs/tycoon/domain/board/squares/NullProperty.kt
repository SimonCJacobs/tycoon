package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class NullProperty : Property() {

    override val listPrice: CurrencyAmount = CurrencyAmount.NULL

    override fun <T> accept( propertyVisitor: PropertyVisitor<T> ): T {
        throw Error( "Not to be used" )
    }

    override fun <T> accept(squareVisitor: SquareVisitor<T>): T {
        throw Error( "Not to be used" )
    }

    override val indexOnBoard: Int = -1
    override val name: String = ""

}
