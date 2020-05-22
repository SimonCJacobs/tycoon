package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
abstract class Property : Square() {

    companion object {
        val NULL = NullProperty()
    }

    abstract val listPrice: CurrencyAmount

    private var mortgaged: Boolean = false

    abstract fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T

    fun canBeMortgaged(): Boolean {
        return this.mortgaged == false &&
            ( this is Street == false || this.hasAnyDevelopment() == false )
    }

    fun isMortgaged(): Boolean {
        return this.mortgaged
    }

    fun mortgageInterestAmount( bankInterestRate: Float ): CurrencyAmount {
        return this.mortgagedValue() * bankInterestRate
    }

    fun mortgagePlusInterest( bankInterestRate: Float ): CurrencyAmount {
        return this.mortgagedValue() + mortgageInterestAmount( bankInterestRate )
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