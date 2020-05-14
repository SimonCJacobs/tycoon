package jacobs.tycoon.domain.actions.trading

import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class TradeOffer (
    val offered: Assets,
    val wanted: Assets,
    val offeringPlayer: Player,
    val offerRecipient: Player
) {

    fun putIntoEffect() {
        offered.transfer( offeringPlayer, offerRecipient )
        wanted.transfer( offerRecipient, offeringPlayer )
    }

}