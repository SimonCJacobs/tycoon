package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyAmount (
    private val amount: Int,
    private val currency: Currency
) {

    companion object {
        val NULL = CurrencyAmount( 0, Currency.NULL )
    }

    operator fun plus( otherCurrencyAmount: CurrencyAmount ): CurrencyAmount {
        return CurrencyAmount( this.amount + otherCurrencyAmount.amount, this.currency )
    }

    operator fun minus( otherCurrencyAmount: CurrencyAmount ): CurrencyAmount {
        return CurrencyAmount( this.amount - otherCurrencyAmount.amount, this.currency )
    }

    override fun toString(): String {
        return this.currency.prefix + this.amount
    }

}