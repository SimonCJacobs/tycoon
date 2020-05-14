package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
abstract class Property : Square() {

    companion object {
        const val INTEREST_RATE = 0.1
        val NULL = NullProperty()
    }

    abstract val listPrice: CurrencyAmount

    private var mortgaged: Boolean = false

    abstract fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T

    fun isMortgaged(): Boolean {
        return this.mortgaged
    }

    fun mortgagePlusInterest(): CurrencyAmount {
        return this.mortgagedValue() * ( 1 + INTEREST_RATE ).toFloat()
    }

    fun mortgagedValue(): CurrencyAmount {
        return this.listPrice.half()
    }

    fun payOffMortgage() {
        this.mortgaged = false
    }

    fun takeOutMortgage() {
        this.mortgaged = true
    }

}