package jacobs.tycoon.integration

import jacobs.tycoon.domain.actions.property.MortgageOnTransferDecision
import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MortgagingTest {

    @Test
    fun canMortgage() {
        return runBlockingMultiplatform {
            val manager = getIntoPositionWhereOwnProperty()
            manager.mortgagePropertyAtIndex( 5, 0 )
            assertTrue( manager.propertyAtIndex( 5 ).isMortgaged(), "King's Cross is mortgaged" )
            assertEquals(
                manager.inCurrency( 1500 - 200 + 100 ),
                manager.getPlayer( 0 ).cashHoldings,
                "Batman's cash includes mortgage value"
            )
        }
    }

    @Test
    fun canRemoveMortgage() {
        return runBlockingMultiplatform {
            val manager = getIntoPositionWhereOwnProperty()
            manager.mortgagePropertyAtIndex( 5, 0 )
            manager.payOffMortgageOnPropertyAtIndex( 5, 0 )
            assertFalse( manager.propertyAtIndex( 5 ).isMortgaged(), "King's Cross is not mortgaged" )
            assertEquals(
                manager.inCurrency( 1500 - 200 + 100 - 110 ),
                manager.getPlayer( 0 ).cashHoldings,
                "Batman's cash shows has paid mortgage interest charge"
            )
        }
    }


    @Test
    fun transferOfMortgagedPropertyIncursCharge() {
        return runBlockingMultiplatform {
            val manager = getIntoPositionWhereOwnProperty()
            manager.mortgagePropertyAtIndex( 5, 0 )
            val offer = TradeOffer(
                Assets( setOf( manager.propertyAtIndex( 5 ) ), manager.inCurrency( 0 ) ),
                Assets( setOf(), manager.inCurrency( 300 ) ),
                manager.getPlayer( 0 ),
                manager.getPlayer( 1 )
            )
            manager.offerTrade( offer, 0 )
            manager.acceptTrade( 1 )                                    // Robin accepts the trade
            manager.payMortgageOnTrade( MortgageOnTransferDecision.PAY_INTEREST_ONLY, 1 )
            assertTrue( manager.propertyAtIndex( 5 ).isMortgaged(), "King's Cross is mortgaged" )
            assertEquals(
                manager.inCurrency( 1500 - 300 - 10 ),
                manager.getPlayer( 1 ).cashHoldings,
                "Robin's cash shows has paid mortgage interest"
            )
        }
    }

    private suspend fun getIntoPositionWhereOwnProperty(): GameStateManager {
        return GameStateManager.new {
            player( "Batman", "Iron" )
            player( "Robin", "Top hat" )
            goToRollForMove()
            rollAndBuy( 3 to 2, 0 )     // Batman get King's Cross
        }
    }


}