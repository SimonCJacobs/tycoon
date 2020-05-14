package jacobs.tycoon.testdata.gamestate

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.GameExecutor
import jacobs.tycoon.domain.GameInitialiser
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.services.auction.Auctioneer
import jacobs.tycoon.domain.services.auction.DumbAuctioneer
import jacobs.tycoon.settings.settingsModule
import jacobs.tycoon.state.GameState
import jacobs.tycoon.state.sharedStateModule
import jacobs.tycoon.testdata.fakes.DiceRollFixed
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

class GameStateBuilder private constructor( kodein: Kodein ) {

    companion object {
        suspend fun new( closure: suspend GameStateBuilder.() -> Unit ): GameStateBuilder {
            val kodein = Kodein {
                bind < Auctioneer >() with singleton { DumbAuctioneer( kodein ) }
                import( domainModule() )
                import( settingsModule() )
                import( sharedStateModule() )
            }
            return GameStateBuilder( kodein )
                .apply {
                    this.gameController.initialise()
                    this.gameInitialiser.initialiseStandardGame( this.executor )
                    closure()
                }
        }
    }

    val gameController by kodein.instance < GameController > ()

    private val executor by kodein.instance < GameExecutor > ( tag = "actual" )
    private val gameInitialiser by kodein.instance < GameInitialiser > ()
    private val gameState by kodein.instance < GameState > ()

    val game: Game
        get() = gameState.game()

    var nextSeatingPosition = 0

    // RUNNING GAME METHODS

    suspend fun player( name: String, pieceName: String ) {
        this.executor.execute(
            AddPlayer(name, gameState.game().board.pieceSet.getPieceByName(pieceName)!!)
                .apply { setPositionOfActor( SeatingPosition( nextSeatingPosition++ ) ) }
        )
    }

    suspend fun completeSignUp() {
        this.executor.execute(CompleteSignUp())
    }

    suspend fun rollForMove( roll: Pair < Int, Int >, position: Int ) {
        RollForOrderAction()
            .apply {
                result = RollForOrderResult( DiceRollFixed( roll.first, roll.second ), RollForOrderOutcome.ROLLING )
                requestFromPosition( position )
            }
    }

    suspend fun goToRollForMove() {
        completeSignUp()
        ( 0 until nextSeatingPosition  ).forEach {
            rollForMove(
                ( if ( it < 6 ) 6 - it else 1 ) to ( if ( it < 6 ) 6 else 11 - it ), // Going down 12, 11, 10, ...
                it
            )
        }
    }

    /**
     * To be used when players playing the game in order of seating position ( as occurs with [goToRollForMove] )
     */
    suspend fun roll( diceRoll: Pair < Int, Int >, playerNumber: Int ) {
        RollForMove()
            .apply { result = RollForMoveResult.ofRollOnly( DiceRollFixed( diceRoll ) ) }
            .requestFromPosition( playerNumber )
    }

    suspend fun doMove( playerNumber: Int ) {
        PieceMoved().requestFromPosition( playerNumber )
    }

    suspend fun respondToPropertyOffer( yesNo: Boolean, playerNumber: Int ) {
        RespondToPropertyOffer(yesNo).requestFromPosition( playerNumber )
    }

    /**
     * To be used when players playing the game in order of seating position ( as occurs with [goToRollForMove] )
     */
    suspend fun rollAndBuy( diceRoll: Pair < Int, Int >, playerNumber: Int ) {
        roll( diceRoll, playerNumber )
        doMove( playerNumber )
        respondToPropertyOffer( true, playerNumber )
    }

    suspend fun makeBid( amount: Int, player: Int ) {
        AuctionBid(amount.toCurrency()).requestFromPosition( player )
    }

    // INFORMATION ON STATE

    fun getPlayer( number: Int ): Player {
        return number.toPlayer()
    }

    fun isTurnOfPlayer( playerNumber: Int ): Boolean {
        return game.isTurnOfPlayer( playerNumber.toPlayer() )
    }

    // HELPERS

    private suspend fun < T : GameAction > T.requestFromPosition( index: Int ) {
        this.apply {
            actorPosition = index.toPosition()
            duplicate( gameController)
        }
    }

    fun inCurrency( value: Int ): CurrencyAmount {
        return value.toCurrency()
    }

    private fun Int.toCurrency(): CurrencyAmount {
        return game.board.currency.ofAmount( this )
    }

    private fun Int.toPlayer(): Player {
        return game.players.getPlayerAtPosition( this.toPosition() )
    }

    private fun Int.toPosition(): SeatingPosition {
        return SeatingPosition( this )
    }


}