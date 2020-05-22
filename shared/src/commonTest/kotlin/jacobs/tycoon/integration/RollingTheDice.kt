package jacobs.tycoon.integration

import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RollingTheDice {

    @Test
    fun doubleMeansAnotherGo() {
        return runBlockingMultiplatform {
            val manager = GameStateManager.new {
                player( "Jack", "Battleship" )
                player( "Jill", "Iron" )
                goToRollForMove()
                rollAndBuy( 3 to 3, 0 )             // Jack buys Angel
            }
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 0 ) ) )
            assertFalse( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ) )
            assertTrue( manager.game.isPhase < RollingForMove > () )
        }
    }

}