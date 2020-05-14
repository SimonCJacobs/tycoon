package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.property.BuildingProject
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.actions.results.ReadCardResult
import jacobs.tycoon.domain.actions.results.RentChargeResult
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PlayingPiece
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
 * From this point on, external controls should be in place so that only one coroutine at a time
 * accesses the code.
 *
 * This class acts as something of a façade to the [Game] class, but having access to the domain
 * services to allow [Game] itself to remain dependency free.
 */
class GameController( kodein: Kodein ) {

    private val gameCycle by kodein.instance <GameCycle> ()
    private val gameExecutor by kodein.instance < ActualGameExecutor > ()
    private val gameFactory by kodein.instance <GameFactory> ()
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

    fun addPlayer( name: String, piece: PlayingPiece, position: SeatingPosition ): Boolean {
        val newPlayerObject = this.playerFactory.getNew( name, piece, position )
        return this.game().addPlayer( newPlayerObject )
    }

    fun build( streets: List < Street >, newHouses: List < Int >, actorPosition: SeatingPosition ): Boolean {
        val buildingProject = BuildingProject(
            streets = streets,
            newHouses = newHouses,
            player = actorPosition.getPlayer( game() ),
            bank = game().bank
        )
        return game().build( buildingProject )
    }

        //TODO actually have another turn to charge rent
    fun chargeRent( actorPosition: SeatingPosition ): RentChargeResult {
        return this.game().chargeRent( gameCycle, actorPosition )
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

    fun offerTrade( tradeOffer: TradeOffer, actorPosition: SeatingPosition ): Boolean {
        return this.game().offerTrade( gameCycle, tradeOffer, actorPosition )
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

    fun rollTheDiceForMove( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? ): RollForMoveResult {
        return game().rollTheDiceForMove( gameCycle, actorPosition, maybeDiceRoll )
    }

    fun rollTheDiceForThrowingOrder( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? )
            : RollForOrderResult {
        return gameState.game().rollTheDiceForThrowingOrder( this.gameCycle, actorPosition, maybeDiceRoll )
    }

    fun setGameBoard( board: Board, shuffleOrders: List < List < Int > >? = null ): Boolean {
        board.initialise( gameRules.housesToAHotel )
        if ( null != shuffleOrders ) board.applyShuffleOrders( shuffleOrders )
        game().board = board
        return true
    }

    fun startGame() {
        this.gameState.setGame( this.gameFactory.newGame() )
    }

}