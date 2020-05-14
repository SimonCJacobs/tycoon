package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.services.auction.BidWarning
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount

class AuctionState() {

    lateinit var bidBeingConsidered: CurrencyAmount
    var bidWarning: BidWarning? = null

    fun reset( currency: Currency ) {
        bidBeingConsidered = CurrencyAmount( 1, currency )
        bidWarning = null
    }

}