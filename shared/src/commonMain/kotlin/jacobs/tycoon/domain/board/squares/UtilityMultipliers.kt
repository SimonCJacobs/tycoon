package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class UtilityMultipliers (
    val withOneUtility: CurrencyAmount,
    val withTwoUtilities: CurrencyAmount
) {

    companion object {

        fun new( multiplierPair: Pair < Int, Int >, currency: Currency ): UtilityMultipliers {
            return UtilityMultipliers(
                CurrencyAmount( multiplierPair.first, currency ),
                CurrencyAmount( multiplierPair.second, currency )
            )
        }

    }

}
