package jacobs.tycoon.domain.phases.results

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
class RentChargeResult(
    val propertyOwner: Player,
    val rentDue: CurrencyAmount,
    val occupyingPlayer: Player,
    val occupiedProperty: Property,
    val wasRentCharged: Boolean
) {

    companion object {
        val NULL = RentChargeResult( Player.NULL, CurrencyAmount.NULL, Player.NULL, Property.NULL, false )
    }

}