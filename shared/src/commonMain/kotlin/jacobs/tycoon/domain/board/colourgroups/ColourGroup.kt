package jacobs.tycoon.domain.board.colourgroups

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount

class ColourGroup(
    val name: String,
    val numberInGroup: Int,
    private val costOfHouse: Int,
    private val currency: Currency
) {
    companion object {
        val NULL = ColourGroup( "", 0, 0, Currency.NULL )
    }

    fun costOfHouse(): CurrencyAmount {
        return CurrencyAmount( costOfHouse , currency )
    }

}