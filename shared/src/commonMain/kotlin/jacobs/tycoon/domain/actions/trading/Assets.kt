package jacobs.tycoon.domain.actions.trading

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class Assets (
    private val properties: Set < Property >,
    private val cash: CurrencyAmount,
    private val getOutOfJailFreeCardsCount: Int
) {

    fun transfer( vendor: Player, acquirer: Player ) {
        properties.forEach {
            vendor.disposeOfProperty( it )
            acquirer.acquireProperty( it )
        }
        vendor.debitFunds( cash )
        acquirer.creditFunds( cash )
        val jailCards = vendor.disposeOfGetOutOfJailFreeCards( getOutOfJailFreeCardsCount )
        jailCards.forEach { acquirer.saveGetOutOfJailFreeCard( it ) }
    }

}