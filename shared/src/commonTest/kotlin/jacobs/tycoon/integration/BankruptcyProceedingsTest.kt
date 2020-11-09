package jacobs.tycoon.integration

import jacobs.tycoon.domain.board.StandardBoard
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.phases.CrownTheVictor
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.testdata.gamestate.GameStateBuilder
import jacobs.tycoon.testdata.gamestate.GameStateManager
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BankruptcyProceedingsTest {

    @Test
    fun rentCanSendIntoBankruptcy() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderTwoPlayer( 105 )
                .apply {
                    rollAndBuy( 2 to 4, 0 )                 // Ken buys Angel
                    rollAndBuy( 2 to 6, 1 )                 // Deidre buys Euston Road
                    roll( 1 to 1, 0 )
                    doMove( 0 )                             // Ken moves to Euston Road
                    chargeRent( 8, 1 )
                    attemptToPay( 0 )
                }
            assertTrue( manager.game.isPhase < BankruptcyProceedings > (), "In bankruptcy proceedings" )
            assertEquals( manager.getPlayer( 0 ), manager.game.getBankruptPlayer(), "Ken is broke" )
        }
    }

    @Test
    fun taxSquareCanSendIntoBankruptcy() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderTwoPlayer( 99 )
                .apply {
                    roll( 3 to 1, 0 )                       // Alvin rolls onto income tax
                    doMove( 0 )                             // Alvin moves
                    attemptToPay( 0 )
                }
            assertTrue( manager.game.isPhase < BankruptcyProceedings > (), "In bankruptcy proceedings" )
            assertEquals( manager.getPlayer( 0 ), manager.game.getBankruptPlayer(),  "Alvin is broke" )
        }
    }

    @Test
    fun billFromCardCanSendIntoBankruptcy() {
        return runBlockingMultiplatform {
            val manager = aboutToGoBankruptOnCommunityChestInThreePlayerGame()
                .apply {
                    doMove( 0 )
                    drawCard( 0 )
                    attemptToPay( 0 )
                }
            assertTrue( manager.game.isPhase < BankruptcyProceedings > (), "In bankruptcy proceedings" )
            assertEquals( manager.getPlayer( 0 ), manager.game.getBankruptPlayer(),  "Alvin is broke" )
        }
    }

    @Test
    fun jailFineCanSendIntoBankruptcy() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderTwoPlayer( 249 )
                .apply {
                    roll( 2 to 2, 0 )                       // Ken rolls onto income tax
                    doMove( 0 )                             // Ken moves
                    attemptToPay( 0 )                       // Pays the £200
                    roll( 3 to 3, 0 )                       // Just visiting jail
                    doMove( 0 )                             // Ken moves
                    roll( 2 to 2, 0 )                       // Ken rolls third double
                    doMove( 0 )                             // Ken now in Jail
                    rollAndBuy( 1 to 2, 1 )                 // Deidre get Whitechapel
                    rollFromJail( 1 to 2, 0 )               // Ken first roll in jail
                    rollAndBuy( 1 to 2, 1 )                 // Deidre get Angel
                    rollFromJail( 1 to 2, 0 )               // Ken second roll in jail
                    roll( 1 to 3, 1 )                       // Deidre to Just Visiting
                    doMove( 1 )                             // Deidre to Just Visiting
                    rollFromJail( 1 to 2, 0 )               // Ken third roll in jail
                    attemptToPay( 0 )
                }
            manager.assertPhase < BankruptcyProceedings > ()
            assertEquals( manager.getPlayer( 0 ), manager.game.getBankruptPlayer(),  "Alvin is broke" )
        }
    }

    @Test
    fun simpleBankruptcyAndPlayMovesToNextPlayer() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderThreePlayer( 99 )
                .apply {
                    roll( 3 to 1, 0 )                       // Alvin rolls onto income tax
                    doMove( 0 )                             // Alvin moves
                    attemptToPay( 0 )
                    goToNextPhaseOfBankruptcy()
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Game moves on" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Simon has turn" )
        }
    }

    @Test
    fun assetsPassToCreditor() {
        return runBlockingMultiplatform {
            val manager = triggerBankruptcyInThreePlayerGame()
                .apply {
                    goToNextPhaseOfBankruptcy()
                }
            assertTrue( manager.game.isPhase < RollingForMove > (), "Next turn" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Simon has turn" )
            assertTrue( manager.getPlayer( 2 ).owns( manager.propertyAtIndex( 3 ) ), "Alvin has Whitechapel" )
        }
    }

    @Test
    fun propertiesAreAuctionedWhenBankIsCreditor() {
        return runBlockingMultiplatform {
            val manager = aboutToGoBankruptOnCommunityChestInThreePlayerGame()
                .apply {
                    doMove( 0 )
                    drawCard( 0 )
                    attemptToPay( 0 )
                    goToNextPhaseOfBankruptcy()
                }
            assertTrue( manager.game.isPhase < AuctionProperty > (), "In auction" )
        }
    }

    @Test
    fun bankruptPlayerRemovedFromGame() {
        return runBlockingMultiplatform {
            val manager = aboutToGoBankruptOnCommunityChestInThreePlayerGame()
                .apply {
                    doMove( 0 )
                    drawCard( 0 )
                    attemptToPay( 0 )
                    goToNextPhaseOfBankruptcy()
                }
            assertEquals( manager.game.players.activeCount(), 2, "Two players remain in game" )
        }
    }

    @Test
    fun winnerIsCrownedInSimpleScenario() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderTwoPlayer( 99 )
                .apply {
                    roll( 3 to 1, 0 )                       // Ken rolls onto income tax
                    doMove( 0 )                             // Ken moves
                    attemptToPay( 0 )
                    goToNextPhaseOfBankruptcy()
                }
            assertTrue( manager.game.isPhase < CrownTheVictor > (), "There is a winner of the game" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Deidre wins the game" )
        }
    }

    @Test
    fun winnerIsCrownedWhenRentCharged() {
        return runBlockingMultiplatform {
            val manager = lowCashStateBuilderTwoPlayer( 151 )
                .apply {
                    rollAndBuy( 1 to 2, 0 )     // Ken buys Whitechapel
                    rollAndBuy( 4 to 2, 1 )     // Deidre buys Angel
                    getPlayer( 0 ).cashHoldings = inCurrency( 4 )       // Steal Ken's money
                    roll( 1 to 2, 0 )           // Put Ken on Angel
                    doMove( 0 )
                    chargeRent( 6, 1 )          // Deidre charges rent
                    attemptToPay( 0 )           // Ken tries to pay
                    goToNextPhaseOfBankruptcy()
                }
            assertTrue( manager.game.isPhase < CrownTheVictor > (), "There is a winner of the game" )
            assertTrue( manager.game.isTurnOfPlayer( manager.getPlayer( 1 ) ), "Deidre wins the game" )
        }
    }

    private suspend fun lowCashStateBuilderTwoPlayer( cashLevel: Int ): GameStateManager {
        return GameStateManager.new ( lowCashOverrideBuilder( cashLevel ) ) {
            player( "Ken", "Racing car" )
            player( "Deidre", "Boot" )
            goToRollForMove()
        }
    }

    private suspend fun lowCashStateBuilderThreePlayer( cashLevel: Int ): GameStateManager {
        return GameStateManager.new ( lowCashOverrideBuilder( cashLevel ) ) {
            player( "Alvin", "Racing car" )
            player( "Simon", "Boot" )
            player( "Theodore", "Battleship" )
            goToRollForMove()
        }
    }

    private suspend fun triggerBankruptcyInThreePlayerGame(): GameStateManager {
        return lowCashStateBuilderThreePlayer( 1000 )
            .apply {
                rollAndBuy( 1 to 2, 0 )     // Alvin buys Whitechapel
                rollAndBuy( 3 to 2, 1 )     // Simon buys King's Cross
                rollAndBuy( 4 to 2, 2 )     // Theodore buys Angel
                getPlayer( 0 ).cashHoldings = inCurrency( 4 )       // Steal Alvin's money
                roll( 1 to 2, 0 )           // Put Alvin on Angel
                doMove( 0 )
                chargeRent( 6, 2 )          // Theodore charges rent
                attemptToPay( 0 )           // Alvin tries to pay
            }
    }

    private suspend fun aboutToGoBankruptOnCommunityChestInThreePlayerGame(): GameStateManager {
        return lowCashStateBuilderThreePlayer( 151 )
            .apply {
                rollAndBuy( 6 to 6, 0 )     // Alvin buys Electric Company
                setIndexOfFirstCard( StandardBoard.COMMUNITY_CHEST_CARDS_NAME, 3 )  // Put Hospital fees £100 at the start
                roll( 3 to 2, 0 )           // Rolls to Community Chest
            }
    }

    private fun lowCashOverrideBuilder( cashLevel: Int ): GameStateBuilder {
        return GameStateBuilder().apply {
            overrides = {
                bind < MiscellaneousRules > ( overrides = true ) with singleton { LowCashRules( cashLevel ) }
            }
        }
    }

    class LowCashRules( cashLevel: Int ) : MiscellaneousRules {
        override val currency: Currency = PoundsSterling()
        override val goCreditAmount: CurrencyAmount = CurrencyAmount( 200, currency )
        override val housesToAHotel: Int = 5
        override val initialCashCount: CurrencyAmount = CurrencyAmount( cashLevel, currency )
        override val initialHotelStock: Int = 12
        override val initialHousingStock: Int = 12
        override val minimumNumberOfPlayers: Int = 2
        override val numberOfTurnsHaveToChargeRent: Int = 2
    }

}