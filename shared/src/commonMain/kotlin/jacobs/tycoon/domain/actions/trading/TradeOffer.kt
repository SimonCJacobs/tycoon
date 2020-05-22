package jacobs.tycoon.domain.actions.trading

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class TradeOffer (
    val offered: Assets,
    val wanted: Assets,
    val offeringPlayer: Player,
    val offerRecipient: Player
) {

    fun actual( game: Game ): TradeOffer {
        return TradeOffer(
            offered = offered.actual( game.board ),
            wanted = wanted.actual( game.board ),
            offeringPlayer = game.players.getActualPlayer( offeringPlayer ),
            offerRecipient = game.players.getActualPlayer( offerRecipient )
        )
    }

    fun putIntoEffect() {
        offered.transfer( offeringPlayer, offerRecipient )
        wanted.transfer( offerRecipient, offeringPlayer )
    }

    fun includesMortgagedProperty(): Boolean {
        return offered.includesMortgagedProperty() || wanted.includesMortgagedProperty()
    }

}