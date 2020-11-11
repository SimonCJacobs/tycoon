package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCardDecision
import jacobs.tycoon.domain.actions.property.BuildingProject
import jacobs.tycoon.domain.actions.property.MortgageOnTransferDecision
import jacobs.tycoon.domain.phases.results.MoveResult
import jacobs.tycoon.domain.phases.results.ReadCardResult
import jacobs.tycoon.domain.phases.results.RentChargeResult
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.phases.results.RollForOrderResult
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.ShuffleOrders
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.results.RollForMoveFromJailResult
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.PlayerFactory
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.services.GameCycle
import jacobs.tycoon.domain.services.GameFactory
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.domain.services.auction.AuctionStatus
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * From this point on, external controls will be in place so that only one coroutine at a time
 * accesses the code.
 *
 * This class acts as something of a façade to the [Game] class, but having access to the domain
 * services to allow [Game] itself to remain dependency free.
 */
class GameController( kodein: Kodein ) {

    private val gameCycle by kodein.instance < GameCycle > ()
    private val gameExecutor by kodein.instance < ActualGameExecutor > ()
    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameRules by kodein.instance < MiscellaneousRules > ()
    private val gameState by kodein.instance < GameState > ()
    private val playerFactory by kodein.instance < PlayerFactory > ()

    /**
     * To break dependency loop with GameExecutor and allow the domain services to send commands to the game
     * itself
     */
    fun initialise() {
        this.gameExecutor.registerGameController( this )
    }

    // REGULAR PUBLIC API

    fun acceptFunds( actorPosition: SeatingPosition ): Boolean {
        return game().acceptFunds( gameCycle, actorPosition )
    }

    fun addPlayer( name: String, piece: PlayingPiece, position: SeatingPosition ): Boolean {
        val newPlayerObject = this.playerFactory.getNew( name, piece, position )
        return this.game().addPlayer( newPlayerObject )
    }

    fun attemptToPay( position: SeatingPosition ): Boolean {
        return game().attemptToPay( gameCycle, position )
    }

    fun build( streets: List < Street >, newHouses: List < Int >, actorPosition: SeatingPosition ): Boolean {
        val buildingProject = BuildingProject(
            streets = game().board.getActualSquares( streets ),
            houseChangeDistribution = newHouses,
            player = actorPosition.getPlayer( game() ),
            bank = game().bank
        )
        return game().build( buildingProject )
    }

    fun carryOutBankruptcyProceedings(): Boolean {
        return game().carryOutBankruptcyProceedings( gameCycle )
    }

    fun chargeRent( property: Property, actorPosition: SeatingPosition ): RentChargeResult {
        return this.game().chargeRent( gameCycle, property, actorPosition )
    }

    fun completePieceMove( position: SeatingPosition ): MoveResult {
        return game().completePieceMove( this.gameCycle, position )
    }

    fun completeSignUp() {
        this.game().completeSignUp( this.gameCycle )
    }

    fun concludeAuction(): AuctionStatus {
        return game().concludeAuction( this.gameCycle )
    }

    suspend fun duplicate( action: GameAction ) {
        action.duplicate( this )
    }

    fun game(): Game {
        return this.gameState.game()
    }

    fun makeAuctionNotification( newPhase: AuctionPhase ): Boolean {
        return game().updateAuctionProceedings( newPhase )
    }

    fun mortgageProperty( property: Property, actorPosition: SeatingPosition): Boolean {
        return game().mortgageProperty( game().board.getActualSquare( property ), actorPosition )
    }

    fun offerTrade( tradeOffer: TradeOffer, actorPosition: SeatingPosition ): Boolean {
        return this.game().offerTrade( gameCycle, tradeOffer.actual( game() ), actorPosition )
    }

    fun payFineOrTakeCard( decision: PayFineOrTakeCardDecision, actorPosition: SeatingPosition ): Boolean {
        return game().payFineOrTakeCard( gameCycle, decision, actorPosition )
    }

    fun payJailFine( seatingPosition: SeatingPosition ): Boolean {
        return this.game().payJailFineVoluntarily( gameCycle, seatingPosition )
    }

    fun payOffMortgage( property: Property, actorPosition: SeatingPosition ): Boolean {
        return this.game().payOffMortgage( game().board.getActualSquare( property ), actorPosition )
    }

    fun payOffMortgageOnTransfer( decision: MortgageOnTransferDecision, actorPosition: SeatingPosition ): Boolean {
        return game().payOffMortgageOnTransfer( gameCycle, decision, actorPosition )
    }

    fun readCard( position: SeatingPosition ): ReadCardResult {
        return this.gameState.game().readCard( this.gameCycle, position )
    }

    fun respondToPropertyOffer( decidedToBuy: Boolean, position: SeatingPosition ) {
        game().respondToPropertyOffer( gameCycle, decidedToBuy, position )
    }

    fun respondToTradeOffer( response: Boolean, actorPosition: SeatingPosition ): Boolean {
        return game().respondToTradeOffer( gameCycle, response, actorPosition )
    }

    fun rollTheDiceFromJailForMove( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? )
            : RollForMoveFromJailResult {
        return game().rollTheDiceFromJailForMove( gameCycle, actorPosition, maybeDiceRoll )
    }

    fun rollTheDiceForMove( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? ): RollForMoveResult {
        return game().rollTheDiceForMove( gameCycle, actorPosition, maybeDiceRoll )
    }

    fun rollTheDiceForThrowingOrder( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? )
            : RollForOrderResult {
        return gameState.game().rollTheDiceForThrowingOrder( this.gameCycle, actorPosition, maybeDiceRoll )
    }

    fun sellProperties( streets: List < Street >, housesToSell: List < Int >, actorPosition: SeatingPosition)
            : Boolean {
        val buildingProject = BuildingProject(
            streets = game().board.getActualSquares( streets ),
            houseChangeDistribution = housesToSell,
            player = actorPosition.getPlayer( game() ),
            bank = game().bank
        )
        return game().sellProperties( buildingProject )
    }

    fun setGameBoard( board: Board, shuffleOrders: ShuffleOrders? = null ): ShuffleOrders {
        game().board = board
        board.initialise( gameRules )
        if ( null != shuffleOrders )
            return board.applyShuffleOrders( shuffleOrders )
        else
            return board.shuffleCards()
    }

    fun startGame() {
        this.gameState.setGame( this.gameFactory.newGame() )
    }

    fun updateCashHoldings( player: Player, newCashAmount: CurrencyAmount ) {
        player.cashHoldings = newCashAmount
    }

    fun useGetOutOfJailFreeCard( seatingPosition: SeatingPosition ): Boolean {
        return game().useGetOutOfJailFreeCard( gameCycle, seatingPosition )
    }

}