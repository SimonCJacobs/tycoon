package jacobs.tycoon.integration

import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.testdata.gamestate.GameStateBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuctionTest {

    @Test
    fun auctionCommencesWhenPurchaseRefused() {
        return runBlockingMultiplatform {
            val builder = this.getUsToAnAuction()
            assertTrue( builder.game.isPhase < AuctionProperty > (), "In auction phase" )
        }
    }

    @Test
    fun auctionBidIsAccepted() {
        return runBlockingMultiplatform {
            val builder = this.getUsToAnAuction()
            builder.makeBid( amount = 50, player = 0 )
            val auctionStatus = builder.game.getAuctionStatus()
            assertEquals( builder.getPlayer( 0 ), auctionStatus.leadingBidderNotNull, "Bill has bid accepted" )
            assertEquals( builder.inCurrency( 50 ), auctionStatus.leadingBidNotNull, "Bill has bid accepted of 50" )
        }
    }

    @Test
    fun multipleBidsArePossible() {
        return runBlockingMultiplatform {
            val builder = this.getUsToAnAuction()
            builder.makeBid( amount = 50, player = 0 )
            builder.makeBid( amount = 61, player = 1 )
            builder.makeBid( amount = 63, player = 0 )
            builder.makeBid( amount = 67, player = 1 )
            builder.makeBid( amount = 125, player = 0 )
            builder.makeBid( amount = 150, player = 1 )
            val auctionStatus = builder.game.getAuctionStatus()
            assertEquals( builder.getPlayer( 1 ), auctionStatus.leadingBidderNotNull, "Ben has leading bid" )
            assertEquals( builder.inCurrency( 150 ), auctionStatus.leadingBidNotNull, "Top bid is Ben's 150" )
        }
    }

    @Test
    fun changeBidPhase() {
        return runBlockingMultiplatform {
            val builder = this.getUsToAnAuction()
            builder.makeBid( amount = 50, player = 0 )
            builder.makeBid( amount = 61, player = 1 )
            builder.makeBid( amount = 63, player = 0 )
            AuctionNotification(AuctionPhase.GOING_ONCE)
                .duplicate( gameController = builder.gameController )
            val auctionStatus = builder.game.getAuctionStatus()
            assertEquals( AuctionPhase.GOING_ONCE, auctionStatus.phase, "Bidding is going once" )
            AuctionNotification(AuctionPhase.GOING_TWICE)
                .duplicate( gameController = builder.gameController )
            assertEquals( AuctionPhase.GOING_TWICE, auctionStatus.phase, "Bidding is going twice" )
        }
    }

    @Test
    fun auctionConcludesWithWinnerBuyingAndMovingToNextTurn() {
        return runBlockingMultiplatform {
            val builder = this.getUsToAnAuction()
            builder.makeBid( amount = 150, player = 0 )
            builder.makeBid( amount = 350, player = 1 )
            val benCashDuringAuction = builder.getPlayer( 1 ).cashHoldings
            ConcludeAuction()
                .duplicate( gameController = builder.gameController )
            assertTrue( builder.game.isPhase < RollingForMove > () )
            assertTrue( builder.game.isTurnOfPlayer( builder.getPlayer( 1 ) ), "It's Ben's turn now" )
            assertTrue(
                builder.getPlayer( 1 ).owns( builder.getPlayer( 0 ).location() as Property ),
                "Ben owns the property Bill is on"
            )
            assertEquals(
                benCashDuringAuction - builder.inCurrency( 350 ),
                builder.getPlayer( 1 ).cashHoldings,
                "Cost Ben 350 big ones for Whitechapel... probably not a good deal"
            )

        }
    }

    private suspend fun getUsToAnAuction(): GameStateBuilder {
        return GameStateBuilder.new {
            player( "Bill", "Boot" )
            player( "Ben", "Horse and rider" )
            goToRollForMove()
            roll( 1 to 2, 0 )                   // Bill goes to Whitechapel Road
            doMove( 0 )                         // Moves there
            respondToPropertyOffer( false, 0 )  // Refuses to buy
        }
    }


}