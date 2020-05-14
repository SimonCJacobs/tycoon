package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class RentCard (
    private val rentList: List < Int >,
    private val currency: Currency
) {

    fun maximumHouseCount(): Int {
        return rentList.size - 1
    }

    fun rentByHouseCount( houseCount: Int ): CurrencyAmount {
        return CurrencyAmount( rentList[ houseCount ], currency )
    }

}
