package jacobs.tycoon.domain.actions.trading

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class Assets (
    private val properties: Collection < Property >,
    private val cash: CurrencyAmount,
    private val getOutOfJailFreeCardsCount: Int = 0
) {

    fun actual( board: Board ): Assets {
        return Assets(
            properties = board.getActualSquares( properties ),
            cash = cash,
            getOutOfJailFreeCardsCount = getOutOfJailFreeCardsCount
        )
    }

    fun forEachMortgaged( callback: ( Property ) -> Unit ) {
        this.properties.filter { it.isMortgaged() }
            .forEach( callback )
    }

    fun includesMortgagedProperty(): Boolean {
        return this.properties.any { it.isMortgaged() }
    }

    fun transfer( vendor: Player, acquirer: Player ) {
        properties.forEach {
            vendor.disposeOfProperty( it )
            acquirer.acquireProperty( it )
        }
        vendor.debitFunds( cash )
        acquirer.creditFunds( cash )
        val jailCards = vendor.disposeOfGetOutOfJailFreeCards( getOutOfJailFreeCardsCount )
        jailCards.forEach { acquirer.acquireGetOutOfJailFreeCard( it ) }
    }

}