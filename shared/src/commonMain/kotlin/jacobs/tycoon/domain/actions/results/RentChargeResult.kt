package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class RentChargeResult( val rentDue: CurrencyAmount ) {

    companion object {
        val NULL = RentChargeResult( CurrencyAmount.NULL )
    }

}