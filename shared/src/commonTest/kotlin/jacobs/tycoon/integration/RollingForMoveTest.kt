package jacobs.tycoon.integration

import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateBuilder
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RollingForMoveTest {

    @Test
    fun rollForOrderAllowsWinnerToGoFirstWhenFirstPlayer() {
        return runBlockingMultiplatform {
            val builder = GameStateBuilder.new {
                player( "Jack", "Battleship" )
                player( "Jill", "Iron" )
                completeSignUp()
                rollForMove( 6 to 4, 0 )
                rollForMove( 2 to 5, 1 )
            }
            assertTrue( builder.game.isTurnOfPlayer( builder.game.players.getByName( "Jack" )!! ) )
            assertFalse( builder.game.isTurnOfPlayer( builder.game.players.getByName( "Jill" )!! ) )
            assertTrue( builder.game.isPhase <RollingForMove> () )
        }
    }

    @Test
    fun rollForOrderAllowsWinnerToGoFirstWhenSecondPlayer() {
        return runBlockingMultiplatform {
            val builder = GameStateBuilder.new {
                player( "Jack", "Battleship" )
                player( "Jill", "Iron" )
                completeSignUp()
                rollForMove( 1 to 4, 0 )
                rollForMove( 2 to 5, 1 )
            }
            assertFalse( builder.game.isTurnOfPlayer( builder.game.players.getByName( "Jack" )!! ) )
            assertTrue( builder.game.isTurnOfPlayer( builder.game.players.getByName( "Jill" )!! ) )
            assertTrue( builder.game.isPhase <RollingForMove> () )
        }
    }

}