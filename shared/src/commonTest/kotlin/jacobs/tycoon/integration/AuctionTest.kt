package jacobs.tycoon.integration

import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuctionTest {

    @Test
    fun auctionCommencesWhenPurchaseRefused() {
        return runBlockingMultiplatform {
            val manager = this.getUsToAnAuction()
            assertTrue( manager.game.isPhase < AuctionProperty > (), "In auction phase" )
        }
    }

    @Test
    fun auctionBidIsAccepted() {
        return runBlockingMultiplatform {
            val manager = this.getUsToAnAuction()
            manager.makeBid( amount = 50, playerNumber = 0 )
            val auctionStatus = manager.game.getAuctionStatus()
            assertEquals( manager.getPlayer( 0 ), auctionStatus.leadingBidderNotNull, "Bill has bid accepted" )
            assertEquals( manager.inCurrency( 50 ), auctionStatus.leadingBidNotNull, "Bill has bid accepted of 50" )
        }
    }

    @Test
    fun multipleBidsArePossible() {
        return runBlockingMultiplatform {
            val manager = this.getUsToAnAuction()
            manager.makeBid( amount = 50, playerNumber = 0 )
            manager.makeBid( amount = 61, playerNumber = 1 )
            manager.makeBid( amount = 63, playerNumber = 0 )
            manager.makeBid( amount = 67, playerNumber = 1 )
            manager.makeBid( amount = 125, playerNumber = 0 )
            manager.makeBid( amount = 150, playerNumber = 1 )
            val auctionStatus = manager.game.getAuctionStatus()
            assertEquals( manager.getPlayer( 1 ), auctionStatus.leadingBidderNotNull, "Ben has leading bid" )
            assertEquals( manager.inCurrency( 150 ), auctionStatus.leadingBidNotNull, "Top bid is Ben's 150" )
        }
    }

    @Test
    fun changeBidPhase() {
        return runBlockingMultiplatform {
            val manager = this.getUsToAnAuction()
            manager.makeBid( amount = 50, playerNumber = 0 )
            manager.makeBid( amount = 61, playerNumber = 1 )
            manager.makeBid( amount = 63, playerNumber = 0 )
            AuctionNotification(AuctionPhase.GOING_ONCE)
                .duplicate( gameController = manager.gameController )
            val auctionStatus = manager.game.getAuctionStatus()
            assertEquals( AuctionPhase.GOING_ONCE, auctionStatus.phase, "Bidding is going once" )
            AuctionNotification(AuctionPhase.GOING_TWICE)
                .duplicate( gameController = manager.gameController )
            assertEquals( AuctionPhase.GOING_TWICE, auctionStatus.phase, "Bidding is going twice" )
        }
    }

    @Test
    fun auctionConcludesWithWinnerBuyingAndMovingToNextTurn() {
        return runBlockingMultiplatform {
            val manager = this.getUsToAnAuction()
            manager.makeBid( amount = 150, playerNumber = 0 )
            manager.makeBid( amount = 350, playerNumber = 1 )
            val benCashDuringAuction = manager.getPlayer( 1 ).cashHoldings
            ConcludeAuction()
                .duplicate( gameController = manager.gameController )
            assertTrue( manager.game.isPhase < RollingForMove > () )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "It's Ben's turn now" )
            assertTrue(
                manager.getPlayer( 1 ).owns( manager.getPlayer( 0 ).location() as Property ),
                "Ben owns the property Bill is on"
            )
            assertEquals(
                benCashDuringAuction - manager.inCurrency( 350 ),
                manager.getPlayer( 1 ).cashHoldings,
                "Cost Ben 350 big ones for Whitechapel... probably not a good deal"
            )

        }
    }

    private suspend fun getUsToAnAuction(): GameStateManager {
        return GameStateManager.new {
            player( "Bill", "Boot" )
            player( "Ben", "Horse and rider" )
            goToRollForMove()
            roll( 1 to 2, 0 )                   // Bill goes to Whitechapel Road
            doMove( 0 )                         // Moves there
            respondToPropertyOffer( false, 0 )  // Refuses to buy
        }
    }


}