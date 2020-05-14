package jacobs.tycoon.domain.services.auction

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

class Auction(
    property: Property,
    private val auctioneer: Auctioneer
) {

    var status: AuctionStatus = auctioneer.startingAuctionStatus( property )

    fun conclude() {
        if ( this.status.areThereBids() ) {
            this.status.phase = AuctionPhase.SOLD
            this.status.leadingBidderNotNull.debitFunds( this.status.leadingBidNotNull )
            this.status.leadingBidderNotNull.acquireProperty( this.status.property )
        }
        else
            this.status.phase = AuctionPhase.UNSOLD
    }

    fun newBid( bidAmount: CurrencyAmount, bidder: Player ): Boolean {
        if ( false == this.status.validateBid( bidAmount, bidder ).isValid )
            return false
        this.status.setBid( bidAmount, bidder )
        this.auctioneer.notifyOfBid()
        return true
    }

    fun update( newPhase: AuctionPhase ): Boolean {
        this.status.phase = newPhase
        return true
    }

}
