package jacobs.tycoon.testdata.gamestate

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.GameExecutor
import jacobs.tycoon.domain.GameInitialiser
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.actions.debt.AttemptToPay
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.phases.results.RollForOrderOutcome
import jacobs.tycoon.domain.phases.results.RollForOrderResult
import jacobs.tycoon.domain.actions.debt.CarryOutBankruptcyProceedings
import jacobs.tycoon.domain.actions.jail.PayJailFineVoluntarily
import jacobs.tycoon.domain.actions.jail.RollForMoveFromJail
import jacobs.tycoon.domain.actions.jail.UseGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.property.Build
import jacobs.tycoon.domain.actions.property.DealWithMortgageOnTransfer
import jacobs.tycoon.domain.actions.property.MortgageOnTransferDecision
import jacobs.tycoon.domain.actions.property.MortgageProperty
import jacobs.tycoon.domain.actions.property.PayOffMortgage
import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.actions.trading.OfferTrade
import jacobs.tycoon.domain.actions.trading.RespondToTradeOffer
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.domainModule
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.results.JailOutcome
import jacobs.tycoon.domain.phases.results.MovingOutOfJailWithDiceResult
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

class GameStateManager private constructor( kodein: Kodein ) {

    companion object {

        suspend fun new( closure: suspend GameStateManager.() -> Unit ): GameStateManager {
            return this.new( GameStateBuilder(), closure )
        }

        suspend fun new( builder: GameStateBuilder,
                         closure: suspend GameStateManager.() -> Unit ): GameStateManager {
            val kodein = Kodein {
                bind < Auctioneer >() with singleton { DumbAuctioneer( kodein ) }
                import( domainModule() )
                import( settingsModule() )
                import( sharedStateModule() )
                builder.overrides.invoke( this )
            }
            return GameStateManager( kodein )
                .apply {
                    this.gameController.initialise()
                    this.gameInitialiser.initialiseStandardGame( this.executor, builder.shuffleOrders )
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

    suspend fun rollFromJail( diceRoll: Pair < Int, Int >, playerNumber: Int ) {
        val dummyResult = MovingOutOfJailWithDiceResult(
            rollForMoveResult = RollForMoveResult.ofRollOnly( DiceRollFixed( diceRoll ) ),
            jailOutcome = JailOutcome.FORCED_TO_PAY_FINE // This field should not be used by game
        )
        RollForMoveFromJail()
            .apply { result = dummyResult }
            .requestFromPosition( playerNumber )
    }

    suspend fun doMove( playerNumber: Int ) {
        PieceMoved().requestFromPosition( playerNumber )
    }

    suspend fun respondToPropertyOffer( yesNo: Boolean, playerNumber: Int ) {
        RespondToPropertyOffer( yesNo ).requestFromPosition( playerNumber )
    }

    /**
     * To be used when players playing the game in order of seating position ( as occurs with [goToRollForMove] )
     */
    suspend fun rollAndBuy( diceRoll: Pair < Int, Int >, playerNumber: Int ) {
        roll( diceRoll, playerNumber )
        doMove( playerNumber )
        respondToPropertyOffer( true, playerNumber )
    }

    suspend fun chargeRent( propertyNumber: Int, playerNumber: Int ) {
        RentCharge( propertyAtIndex( propertyNumber ) ).requestFromPosition( playerNumber )
    }

    suspend fun drawCard( playerNumber: Int ): String {
        return ReadCard().run {
            requestFromPosition( playerNumber )
            result.cardText
        }
    }

    suspend fun attemptToPay( playerNumber: Int ) {
        AttemptToPay().requestFromPosition( playerNumber )
    }

    suspend fun makeBid( amount: Int, playerNumber: Int ) {
        AuctionBid(amount.toCurrency()).requestFromPosition( playerNumber )
    }

    // TRADING

    suspend fun offerTrade( tradeOffer: TradeOffer, playerNumber: Int ) {
        OfferTrade( tradeOffer ).requestFromPosition( playerNumber )
    }

    suspend fun acceptTrade( playerNumber: Int ) {
        RespondToTradeOffer( true ).requestFromPosition( playerNumber )
    }

    suspend fun offerCashForProperty( cashAmount: Int, propertyIndex: Int, targetPlayerIndex: Int,
                                      playerIndex: Int ) {
        val offer = TradeOffer(
            Assets( emptyList(), inCurrency( cashAmount ) ),
            Assets( listOf( propertyAtIndex( propertyIndex ) ), inCurrency( 0 ) ),
            getPlayer( playerIndex ),
            getPlayer( targetPlayerIndex )
        )
        offerTrade( offer, playerIndex )
    }

    // BANKRUPTCY

    suspend fun goToNextPhaseOfBankruptcy() {
        CarryOutBankruptcyProceedings().request()
    }

    // JAIL

    suspend fun payJailFine( playerNumber: Int ) {
        PayJailFineVoluntarily().requestFromPosition( playerNumber )
    }

    suspend fun useGOOJFCard( playerNumber: Int ) {
        UseGetOutOfJailFreeCard().requestFromPosition( playerNumber )
    }

    // MORTGAGING

    suspend fun mortgagePropertyAtIndex( propertyNumber: Int, playerNumber: Int ) {
        MortgageProperty( listOf( this.propertyAtIndex( propertyNumber ) ) )
            .requestFromPosition( playerNumber )
    }

    suspend fun payOffMortgageOnPropertyAtIndex( propertyNumber: Int, playerNumber: Int ) {
        PayOffMortgage( listOf( this.propertyAtIndex( propertyNumber ) ) )
            .requestFromPosition( playerNumber )
    }

    suspend fun payMortgageOnTrade( decision: MortgageOnTransferDecision, playerNumber: Int  ) {
        DealWithMortgageOnTransfer( decision ).requestFromPosition( playerNumber )
    }

    // BUILDING

    suspend fun build( propertyIndices: List < Int >, houseCounts: List < Int >, playerIndex: Int ) {
        Build( propertyIndices.map { propertyAtIndex( it ) as Street }, houseCounts )
            .requestFromPosition( playerIndex )
    }

    // INFORMATION ON STATE

    inline fun < reified T : GamePhase > assertPhase() {
        val currentPhase = game.phaseStatus.current()
        if ( false == currentPhase is T )
            throw Error ( "Assertion failed: Expected current phase of type ${ T::class } but was ${ currentPhase::class } ")
    }

    fun getPlayer( number: Int ): Player {
        return number.toPlayer()
    }

    fun isTurnOfPlayer( playerNumber: Int ): Boolean {
        return game.isTurnOfPlayer( playerNumber.toPlayer() )
    }

    fun propertyAtIndex( index: Int ): Property {
        return game.board.squareList[ index ] as Property
    }

    // HELPERS

    private suspend fun < T : GameAction > T.request() {
        this.apply {
            duplicate( gameController )
        }
    }

    private suspend fun < T : GameAction > T.requestFromPosition( index: Int ) {
        this.apply {
            actorPosition = index.toPosition()
            duplicate( gameController)
        }
    }

    fun inCurrency( value: Int ): CurrencyAmount {
        return value.toCurrency()
    }

    fun setIndexOfFirstCard( deckName: String, index: Int ) {
        game.board.getNamedCardSet( deckName ).pullCardOfIndexToFrontOfDeck( index )
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