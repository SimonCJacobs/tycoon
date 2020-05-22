package jacobs.tycoon.integration

import jacobs.tycoon.domain.phases.PaymentDue
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PayRentTest {

    @Test
    fun rentDueWhenLandOnProperty() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 1 to 2, 0 )         // Legolas buys Whitechapel Road
                    roll( 1 to 2, 1 )               // Gimli moves to Whitechapel Road
                    doMove( 1 )
                    chargeRent( 3, 0 )                 // Legolas charges rent
                }
            assertTrue( manager.game.isPhase < PaymentDue > (), "Payment is due" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 0 ) ), "Is Legolas's turn" )
        }
    }

    @Test
    fun canPayRent() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 1 to 2, 0 )         // Legolas buys Whitechapel Road
                    roll( 1 to 2, 1 )               // Gimli moves to Whitechapel Road
                    doMove( 1 )
                    chargeRent( 3, 0 )                 // Legolas charges rent
                    attemptToPay( 1 )               // Gimli can pay
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Now time to roll" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 0 ) ), "Is Legolas's turn" )
            assertEquals( manager.inCurrency( 1500 - 4 ), manager.getPlayer( 1 ).cashHoldings, "Gimli paid £4 rent" )
        }
    }

    @Test
    fun canPayRentAndGameContinues() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 1 to 2, 0 )         // Legolas buys Whitechapel Road
                    roll( 1 to 2, 1 )               // Gimli moves to Whitechapel Road
                    doMove( 1 )
                    chargeRent( 3, 0 )                 // Legolas charges rent
                    attemptToPay( 1 )               // Gimli can pay
                    rollAndBuy( 1 to 2, 0 )         // Legolas continues and buys Angel
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Now time to roll" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Is Gimli's turn" )
        }
    }

    @Test
    fun canForgetToChargeRent() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 1 to 2, 0 )         // Legolas buys Whitechapel Road
                    roll( 1 to 2, 1 )               // Gimli moves to Whitechapel Road
                    doMove( 1 )
                    rollAndBuy( 1 to 2, 0 )         // Legolas goes on with life and buys Angel
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Now time to roll" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Is Gimli's turn" )
        }
    }

    @Test
    fun payCorrectRentOnUtility() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 6 to 6, 0 )         // Legolas buys electric company
                    rollAndBuy( 1 to 2, 0 )
                    roll( 6 to 6, 1 )
                    doMove( 1 )
                    chargeRent( 12, 0 )
                    attemptToPay( 1 )
                }
            assertEquals( manager.inCurrency( 1500 - 4 * 12 ), manager.getPlayer( 1 ).cashHoldings, "Gimli paid £48 rent" )
        }
    }

    @Test
    fun doublesStillGiveAnotherGoWhenChargeRent() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 4 to 2, 0 )     // Legolas buys Angel
                    roll( 3 to 3, 1 )           // Gimli rolls to Angel
                    doMove( 1 )
                    chargeRent( 6, 0 )
                    attemptToPay( 1 )
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Now time to roll" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Is Gimli's turn because of the double" )
        }
    }

    @Test
    fun doublesStillGiveAnotherGoWhenForgetToChargeRent() {
        return runBlockingMultiplatform {
            val manager = getManagerAtGameStart()
                .apply {
                    rollAndBuy( 4 to 2, 0 )     // Legolas buys Angel
                    roll( 3 to 3, 1 )           // Gimli rolls to Angel
                    doMove( 1 )
                }
            manager.assertPhase < RollingForMove > ()
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Is Gimli's turn because of the double" )
        }
    }

    private suspend fun getManagerAtGameStart(): GameStateManager {
        return GameStateManager.new {
            player( "Legolas", "Racing car" )
            player( "Gimli", "Boot" )
            goToRollForMove()
        }
    }
}