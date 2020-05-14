package jacobs.tycoon.integration

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class GoToJailTest {

    @Test
    fun threeDoublesInARowMeansJail() {
        return runBlockingMultiplatform {
            val builder = rollJacksThirdDouble()
            assertTrue( builder.isTurnOfPlayer( 0 ) )
            assertTrue( builder.game.isThisTheSquareToMoveTo( builder.game.board.jailSquare ) )
            assertTrue( builder.game.isPhase < MovingAPiece > () )
        }
    }

    @Test
    fun doNotLeaveJailWhenNotThrowingADouble() {
        return runBlockingMultiplatform {
            val builder = toJacksFirstGoInJail()
                .apply { roll( 5 to 6, 0 ) }
            assertTrue( builder.game.isPhase < RollingForMove > () )
            assertTrue( builder.isTurnOfPlayer( 1 ), "It is Jill's turn" )
        }
    }

    @Test
    fun doubleGetsYouOutOfJail() {
        return runBlockingMultiplatform {
            val builder = toJacksFirstGoInJail()
                .apply {
                    roll( 5 to 6, 0)           // Jack's first go in jail
                    rollAndBuy( 5 to 2, 1)    // Jill to Vine Street
                    roll( 1 to 1, 0 )         // Double for Jack
                }
            assertTrue( builder.game.isPhase < MovingAPiece > () )
            assertTrue( builder.isTurnOfPlayer( 0 ), "It is Jack's turn" )
        }
    }

    @Test
    fun leaveJailOnThirdNonDoubleAndPayFine() {
        return runBlockingMultiplatform {
            val builder = toJacksFirstGoInJail()
                .apply {
                    roll( 5 to 6, 0 )           // Jack's first go in jail
                    rollAndBuy( 5 to 2, 1  )    // Jill to Vine Street
                    roll( 2 to 1, 0 )           // Jack's second turn in jail
                    rollAndBuy( 3 to 2, 1 )     // Jill gobbling up the properties on Fenchurch St.
                }
            val jacksCashBeforeRollThree = builder.getPlayer( 0 ).cashHoldings
            builder.roll( 4 to 3, 0 )           // Jack's third roll in jail
            assertTrue( builder.game.isPhase < MovingAPiece > () )
            assertTrue( builder.isTurnOfPlayer( 0 ), "It is Jack's turn still" )
            assertEquals(
                jacksCashBeforeRollThree - CurrencyAmount( 50, builder.game.board.currency ),
                builder.getPlayer( 0 ).cashHoldings,
                "Paid £50 fine in jail"
            )
        }
    }

    private suspend fun rollJacksThirdDouble(): GameStateBuilder {
        return GameStateBuilder.new {
            player( "Jack", "Battleship" )
            player( "Jill", "Iron" )
            goToRollForMove()
            rollAndBuy( 3 to 3, 0 )  // Jack to Angel
            rollAndBuy( 2 to 3, 1 )  // Jill to King's X
            rollAndBuy( 1 to 1, 0 )  // Jack to Euston Road
            rollAndBuy( 2 to 2, 1 )  // Jill to Pentonville Road
            roll( 1 to 1, 0 )
        }
    }

    private suspend fun toJacksFirstGoInJail(): GameStateBuilder {
        return rollJacksThirdDouble()
            .apply {
                doMove( 0 )
                rollAndBuy( 2 to 1, 1 )  // Jill to Electric Company
            }
    }

}