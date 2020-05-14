package jacobs.tycoon.domain.services.auction

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
data class AuctionStatus(
    val property: Property,
    private var maybeLeadingBid: CurrencyAmount?,
    private var maybeLeadingBidder: Player?,
    var phase: AuctionPhase
) {
    companion object {
        val NULL = AuctionStatus( Property.NULL, null, null, AuctionPhase.UNSOLD )
    }

    val leadingBidNotNull : CurrencyAmount
        get() = maybeLeadingBid!!
    val leadingBidderNotNull : Player
        get() = maybeLeadingBidder!!

    fun areThereBids(): Boolean {
        return maybeLeadingBid != null
    }

    fun validateBid( bid: CurrencyAmount, bidder: Player ): BidValidation {
        return when {
            bid.isZero() -> false to BidWarning.TOO_LOW
            this.areThereBids() == false -> true to null
            bidder == leadingBidderNotNull -> false to BidWarning.HAVE_TOP_BID
            bid > bidder.cashHoldings -> false to BidWarning.INSUFFICIENT_CASH
            leadingBidNotNull >= bid -> false to BidWarning.TOO_LOW
            else -> true to null
        }
            .run { BidValidation( first, second ) }
    }

    fun setBid( bid: CurrencyAmount, bidder: Player ) {
        this.maybeLeadingBid = bid
        this.maybeLeadingBidder = bidder
    }

    data class BidValidation( val isValid: Boolean, val warning: BidWarning? )

}