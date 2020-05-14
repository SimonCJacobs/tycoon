package jacobs.tycoon.domain.actions.results

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class RentChargeResult(
    val rentDue: CurrencyAmount,
    val outcome: RentChargeOutcome
) {

    companion object {
        val NULL = RentChargeResult( CurrencyAmount.NULL, RentChargeOutcome.RENT_PAID )
    }

}