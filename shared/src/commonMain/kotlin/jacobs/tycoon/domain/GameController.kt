package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.actions.results.ReadCardResult
import jacobs.tycoon.domain.actions.results.RentChargeResult
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.PlayerFactory
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.services.GameCycle
import jacobs.tycoon.domain.services.GameFactory
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
    private val gameFactory by kodein.instance <GameFactory> ()
    private val gameState by kodein.instance < GameState > ()
    private val playerFactory by kodein.instance < PlayerFactory > ()

    fun addPlayer( name: String, piece: PlayingPiece, position: SeatingPosition ): Boolean {
        val newPlayerObject = this.playerFactory.getNew( name, piece, position )
        return this.game().addPlayer( newPlayerObject )
    }

    fun chargeRent( actorPosition: SeatingPosition ): RentChargeResult {
        return this.game().chargeRent( gameCycle, actorPosition )
    }

    fun completePieceMove( position: SeatingPosition ): MoveResult {
        return game().completePieceMove( this.gameCycle, position )
    }

    fun completeSignUp() {
        this.game().completeSignUp( this.gameCycle )
    }

    suspend fun duplicate( action: GameAction ) {
        action.duplicate( this )
    }

    fun game(): Game {
        return this.gameState.game()
    }

    fun readCard( position: SeatingPosition ): ReadCardResult {
        return this.gameState.game().readCard( this.gameCycle, position )
    }

    fun respondToPropertyOffer( decidedToBuy: Boolean, position: SeatingPosition ) {
        game().respondToPropertyOffer( gameCycle, decidedToBuy, position )
    }

    fun rollTheDiceForMove( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? ): RollForMoveResult {
        return game().rollTheDiceForMove( gameCycle, actorPosition, maybeDiceRoll )
    }

    fun rollTheDiceForThrowingOrder( actorPosition: SeatingPosition, maybeDiceRoll: DiceRoll? )
            : RollForOrderResult {
        return gameState.game().rollTheDiceForThrowingOrder( this.gameCycle, actorPosition, maybeDiceRoll )
    }

    fun startGame() {
        this.gameState.setGame( this.gameFactory.newGame() )
    }

}