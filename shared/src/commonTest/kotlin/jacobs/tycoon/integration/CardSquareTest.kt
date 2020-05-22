package jacobs.tycoon.integration

import jacobs.tycoon.domain.board.StandardBoard
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.testdata.gamestate.GameStateBuilder
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CardSquareTest {

    @Test
    fun drawCardWorks() {
        return runBlockingMultiplatform {
            val manager = getUsToACardSquare()
            val cardText = manager.drawCard( 0 )                       // Bill reads "Advance to Go" card.
            assertEquals( "Advance to \"Go\"", cardText, "Gets pass go text" )
            assertTrue( manager.game.isPhase < MovingAPiece > (), "Bill is moving a piece" )
            assertTrue( manager.isTurnOfPlayer( 0 ), "Bill has the turn" )
        }
    }

    private suspend fun getUsToACardSquare(): GameStateManager {
        return GameStateManager.new( straightShuffleOrdersBuilder() ) {
            player( "Bill", "Boot" )
            player( "Ben", "Horse and rider" )
            goToRollForMove()
            roll( 5 to 2, 0 )                   // Bill goes to Chance
            doMove( 0 )                         // Moves there
        }
    }

    private fun straightShuffleOrdersBuilder(): GameStateBuilder {
        return GameStateBuilder().apply {
            shuffleOrders = listOf( StandardBoard.CHANCE_CARDS_NAME, StandardBoard.COMMUNITY_CHEST_CARDS_NAME )
                    .associateWith { ( 0 .. 15 ).toList() }
        }
    }

}