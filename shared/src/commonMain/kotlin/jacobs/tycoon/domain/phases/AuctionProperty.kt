package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.domain.services.auction.Auctioneer

class AuctionProperty (
    override val playerWithTurn: Player,
    property: Property,
    auctioneer: Auctioneer
) : TurnBasedPhase {

    val auction = auctioneer.startAuction( property )

    fun concludeAuction() {
        auction.conclude()
    }

    fun makeBid( bidAmount: CurrencyAmount, bidder: Player ): Boolean {
        return auction.newBid( bidAmount, bidder )
    }

    fun updateAuction( newPhase: AuctionPhase ): Boolean {
        return this.auction.update( newPhase )
    }

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}