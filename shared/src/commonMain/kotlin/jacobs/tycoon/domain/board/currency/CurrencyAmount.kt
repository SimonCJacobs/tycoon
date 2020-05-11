package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class CurrencyAmount (
    private val amount: Int,
    private val currency: Currency
) {

    companion object {
        val NULL = CurrencyAmount( 0, Currency.NULL )
    }

    init {
        if ( amount < 0 ) throw Error( "Currency amounts of less than zero are not permitted" )
    }

    operator fun compareTo( otherCurrencyAmount: CurrencyAmount ): Int {
        return this.amount - otherCurrencyAmount.amount
    }

    fun half(): CurrencyAmount {
        return CurrencyAmount( this.amount / 2, currency )
    }

    operator fun minus( otherCurrencyAmount: CurrencyAmount ): CurrencyAmount {
        return CurrencyAmount( this.amount - otherCurrencyAmount.amount, this.currency )
    }

    operator fun plus( otherCurrencyAmount: CurrencyAmount ): CurrencyAmount {
        return CurrencyAmount( this.amount + otherCurrencyAmount.amount, this.currency )
    }

    operator fun times( multiplier: Int ): CurrencyAmount {
        return CurrencyAmount( this.amount * multiplier, currency )
    }

    operator fun times( multiplier: Float ): CurrencyAmount {
        return CurrencyAmount( this.amount * multiplier.roundToInt(), currency )
    }

    override fun toString(): String {
        return this.currency.prefix + this.amount
    }

}