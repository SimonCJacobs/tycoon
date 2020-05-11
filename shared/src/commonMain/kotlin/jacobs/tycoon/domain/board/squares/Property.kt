package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
abstract class Property : Square() {

    abstract val listPrice: CurrencyAmount

    private var mortgaged: Boolean = false

    abstract fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T

    fun isMortgaged(): Boolean {
        return this.mortgaged
    }

}