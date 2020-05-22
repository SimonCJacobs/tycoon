package jacobs.tycoon.integration

import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RollingForMoveTest {

    @Test
    fun rollForOrderAllowsWinnerToGoFirstWhenFirstPlayer() {
        return runBlockingMultiplatform {
            val manager = GameStateManager.new {
                player( "Jack", "Battleship" )
                player( "Jill", "Iron" )
                completeSignUp()
                rollForMove( 6 to 4, 0 )
                rollForMove( 2 to 5, 1 )
            }
            assertTrue( manager.game.isTurnOfPlayer( manager.game.players.getByName( "Jack" )!! ) )
            assertFalse( manager.game.isTurnOfPlayer( manager.game.players.getByName( "Jill" )!! ) )
            assertTrue( manager.game.isPhase < RollingForMove > () )
        }
    }

    @Test
    fun rollForOrderAllowsWinnerToGoFirstWhenSecondPlayer() {
        return runBlockingMultiplatform {
            val manager = GameStateManager.new {
                player( "Jack", "Battleship" )
                player( "Jill", "Iron" )
                completeSignUp()
                rollForMove( 1 to 4, 0 )
                rollForMove( 2 to 5, 1 )
            }
            assertFalse( manager.game.isTurnOfPlayer( manager.game.players.getByName( "Jack" )!! ) )
            assertTrue( manager.game.isTurnOfPlayer( manager.game.players.getByName( "Jill" )!! ) )
            assertTrue( manager.game.isPhase < RollingForMove > () )
        }
    }

}