package jacobs.tycoon.integration

import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.TradeBeingConsidered
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TradingTest {

    @Test
    fun simpleTradeIsConsidered() {
        return runBlockingMultiplatform {
            val manager = this.doSimpleTradeOffer()
            assertTrue( manager.game.isPhase < TradeBeingConsidered > (), "Trade being considered" ) // Trade being considered
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 2 ) ), "Still Theodore's turn" )
        }
    }

    @Test
    fun simpleTradeWorks() {
        return runBlockingMultiplatform {
            val manager = this.doSimpleTradeOffer()
            manager.acceptTrade( 1 )
            manager.getPlayer( 0 )                          // Check Alvin's property
                .apply {
                    assertEquals( cashHoldings, manager.inCurrency( 1500 - 60 - 100 ), "Alvin has expected cash" )
                    assertFalse( owns( manager.propertyAtIndex( 3 ) ), "Does not own Whitechapel Road" )
                    assertTrue( owns( manager.propertyAtIndex( 5 ) ), "Owns King's Cross" )
                }
            manager.getPlayer( 1 )                          // Check Simon's property
                .apply {
                    assertEquals( cashHoldings, manager.inCurrency( 1500 - 200 + 100 ), "Simon has expected cash" )
                    assertTrue( owns( manager.propertyAtIndex( 3 ) ), "Owns Whitechapel Road" )
                    assertFalse( owns( manager.propertyAtIndex( 5 ) ), "Does not own King's Cross" )
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "In rolling for move" )    // Back to Theodore's go
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 2 ) ), "Still Theodore's turn" )
        }
    }

    @Test
    fun simpleTradeWorksWhenAboutToMovePiece() {
        return runBlockingMultiplatform {
            val manager = getIntoPositionWherePropertyOwned()
            manager.roll( 2 to 4, 2 )               // Alvin rolls
            manager.offerSimpleTrade()
            manager.acceptTrade( 1 )
            manager.assertPhase < MovingAPiece > ()
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 2 ) ), "Still Theodore's turn" )
        }
    }

    private suspend fun doSimpleTradeOffer(): GameStateManager {
        val manager = getIntoPositionWherePropertyOwned()
        manager.offerSimpleTrade()
        return manager
    }

    private suspend fun GameStateManager.offerSimpleTrade() {
        val offer = TradeOffer(
            offered = Assets( setOf( this.propertyAtIndex( 3 ) ), this.inCurrency( 100 ), 0 ),
            wanted = Assets( setOf( this.propertyAtIndex( 5 ) ), this.inCurrency( 0 ), 0 ),
            offeringPlayer = this.getPlayer( 0 ),            // Alvin offering
            offerRecipient = this.getPlayer( 1 )             // to Simon
        )
        this.offerTrade( offer, 0 )                          // Alvin makes offer
    }

    private suspend fun getIntoPositionWherePropertyOwned(): GameStateManager {
        return GameStateManager.new {
            player( "Alvin", "Racing car" )
            player( "Simon", "Boot" )
            player( "Theodore", "Battleship" )
            goToRollForMove()
            rollAndBuy( 1 to 2, 0 )                     // Alvin buys Whitechapel Road
            rollAndBuy( 3 to 2, 1 )                     // Simon buys King's Cross
        }
    }

}