package jacobs.tycoon.integration

import jacobs.tycoon.domain.board.StandardBoard
import jacobs.tycoon.domain.board.cards.GetOutOfJailFreeCard
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.testdata.gamestate.GameStateManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GoToJailTest {

    @Test
    fun threeDoublesInARowMeansJail() {
        return runBlockingMultiplatform {
            val manager = rollJacksThirdDouble()
            assertTrue( manager.isTurnOfPlayer( 0 ) )
            assertTrue( manager.game.isThisTheSquareToMoveTo( manager.game.board.jailSquare ) )
            assertTrue( manager.game.isPhase < MovingAPiece > () )
        }
    }

    @Test
    fun doNotLeaveJailWhenNotThrowingADouble() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
                .apply { rollFromJail( 5 to 6, 0 ) }
            assertTrue( manager.game.isPhase < RollingForMove > () )
            assertTrue( manager.isTurnOfPlayer( 1 ), "It is Jill's turn" )
        }
    }

    @Test
    fun doubleGetsYouOutOfJail() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
                .apply {
                    rollFromJail( 5 to 6, 0 )           // Jack's first go in jail
                    rollAndBuy( 5 to 3, 1 )            // Jill to Pall Mall
                    rollFromJail( 1 to 1, 0 )         // Double for Jack
                }
            assertTrue( manager.game.isPhase < MovingAPiece > () )
            assertTrue( manager.isTurnOfPlayer( 0 ), "It is Jack's turn" )
        }
    }

    @Test
    fun leaveJailOnThirdNonDoubleAndPayFine() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
                .apply {
                    rollFromJail( 5 to 6, 0 )           // Jack's first go in jail
                    rollAndBuy( 5 to 3, 1 )             // Jill to Pall Mall
                    rollFromJail( 2 to 1, 0 )           // Jack's second turn in jail
                    rollAndBuy( 1 to 2, 1 )             // Jill gobbling up the properties on Northumberland Ave.
                }
            val jacksCashBeforeRollThree = manager.getPlayer( 0 ).cashHoldings
            manager.rollFromJail( 4 to 3, 0 )           // Jack's third roll in jail
            manager.attemptToPay( 0 )          // Jack's third roll in jail
            assertTrue( manager.game.isPhase < MovingAPiece > (), "In moving piece phase" )
            assertTrue( manager.isTurnOfPlayer( 0 ), "It is Jack's turn still" )
            assertEquals(
                jacksCashBeforeRollThree - CurrencyAmount( 50, manager.game.board.currency ),
                manager.getPlayer( 0 ).cashHoldings,
                "Paid £50 fine in jail"
            )
        }
    }

    @Test
    fun canUseGetOutOfJailFreeCard() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
            val jacksCash = manager.getPlayer( 0 ).cashHoldings
            val goojfCard = GetOutOfJailFreeCard(
                0, manager.game.board.getNamedCardSet( StandardBoard.CHANCE_CARDS_NAME )
            )
            manager.getPlayer( 0 ).acquireGetOutOfJailFreeCard( goojfCard )
            manager.useGOOJFCard( 0 )
            assertTrue( manager.game.isPhase < RollingForMove > (), "In moving piece phase" )
            assertTrue( manager.isTurnOfPlayer( 0 ), "It is Jack's turn still" )
            assertEquals(
                jacksCash,
                manager.getPlayer( 0 ).cashHoldings,
                "Cost jack nothing to use get out of jail free card"
            )
        }
    }

    @Test
    fun canPayFineVoluntarily() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
            val jacksCash = manager.getPlayer( 0 ).cashHoldings
            manager.payJailFine( 0 )
            assertTrue( manager.game.isPhase < RollingForMove > (), "In moving piece phase" )
            assertTrue( manager.isTurnOfPlayer( 0 ), "It is Jack's turn still" )
            assertEquals(
                jacksCash - manager.inCurrency( 50 ),
                manager.getPlayer( 0 ).cashHoldings,
                "Cost jack nothing to use get out of jail free card"
            )
        }
    }

    @Test
    fun canPayFineVoluntarilyAndMove() {
        return runBlockingMultiplatform {
            val manager = toJacksFirstGoInJail()
            manager.payJailFine( 0 )
            manager.rollAndBuy( 1 to 1, 0 )         // Jack can roll and buy Electric company
            assertTrue(
                manager.getPlayer( 0 ).owns( manager.propertyAtIndex( 12 )
            ), "Jack owns Electric Company" )  // Jack owns Electric company
        }
    }

    private suspend fun rollJacksThirdDouble(): GameStateManager {
        return GameStateManager.new {
            player( "Jack", "Battleship" )
            player( "Jill", "Iron" )
            goToRollForMove()
            rollAndBuy( 3 to 3, 0 )  // Jack to Angel
            rollAndBuy( 1 to 1, 0 )  // Jack to Euston Road
            roll( 1 to 1, 0 )
        }
    }

    private suspend fun toJacksFirstGoInJail(): GameStateManager {
        return rollJacksThirdDouble()
            .apply {
                doMove( 0 )
                rollAndBuy( 2 to 1, 1 )  // Jill to Whitechapel Road
            }
    }

}