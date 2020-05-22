package jacobs.tycoon.integration

import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildingTest {

    @Test
    fun buildingAHouseWorks() {
        return runBlockingMultiplatform {
            val manager = getToWhereAllOfOneSetOwned()
            manager.build( listOf( 6 ), listOf( 1 ), 0 )
            assertEquals( 1, ( manager.propertyAtIndex( 6 ) as Street ).numberOfHousesBuilt, "Angel has one house" )
        }
    }

    @Test
    fun buildingSeveralHousesWorks() {
        return runBlockingMultiplatform {
            val manager = getToWhereAllOfOneSetOwned()
            manager.build( listOf( 6, 8, 9 ), listOf( 1, 1, 2 ), 0 )
            assertEquals( 1, ( manager.propertyAtIndex( 6 ) as Street ).numberOfHousesBuilt, "Angel has one house" )
            assertEquals( 1, ( manager.propertyAtIndex( 8 ) as Street ).numberOfHousesBuilt, "Euston Rd has one house" )
            assertEquals( 2, ( manager.propertyAtIndex( 9 ) as Street ).numberOfHousesBuilt, "Pent. Rd has two houses" )
        }
    }

    private suspend fun getToWhereAllOfOneSetOwned(): GameStateManager {
        return GameStateManager.new {
            player( "A", "Battleship" )
            player( "B", "Boot" )
            player( "C", "Top hat" )
            goToRollForMove()
            rollAndBuy( 2 to 4, 0 )         // A gets Angel
            rollAndBuy( 2 to 6, 1 )         // B gets Euston Road
            rollAndBuy( 3 to 6, 2 )         // C gets Pentonville Road
            offerCashForProperty( cashAmount = 100, propertyIndex = 8, targetPlayerIndex = 1, playerIndex = 0 )
            acceptTrade( 1 )
            offerCashForProperty( cashAmount = 100, propertyIndex = 9, targetPlayerIndex = 2, playerIndex = 0 )
            acceptTrade( 2 )
        }
    }

}