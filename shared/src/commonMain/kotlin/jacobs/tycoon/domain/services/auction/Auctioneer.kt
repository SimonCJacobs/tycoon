package jacobs.tycoon.domain.services.auction

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

abstract class Auctioneer( kodein: Kodein ) {

    private val currency by kodein.instance < Currency >()

    abstract fun notifyOfBid()

    fun startAuction( property: Property ): Auction {
        this.startAuctionTimer()
        return Auction( property, this )
    }

    fun startingAuctionStatus( property: Property ): AuctionStatus {
        return AuctionStatus(
            property = property,
            maybeLeadingBid = null,
            maybeLeadingBidder = null,
            phase = AuctionPhase.BIDDING_STARTED
        )
    }

    protected abstract fun startAuctionTimer()

}