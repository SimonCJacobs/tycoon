package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.Serializable

@Serializable
abstract class Currency {

    companion object {
        val NULL = NullCurrency()
    }

    abstract val prefix: String

    fun ofAmount( amount: Int ): CurrencyAmount {
        return CurrencyAmount( amount, this )
    }

}