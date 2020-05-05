package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.RollTheDice
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@Suppress( "DeferredIsResult" )
class GameController( kodein: Kodein ) : GameExecutor {

    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameState by kodein.instance < GameState > ()

        // TODO: Consider putting this into a more elegant pattern
    suspend fun duplicate( action: GameAction ) {
        when( action ) {
            is RollTheDice -> this.rollTheDice( action, action.diceRoll )
            else -> this.execute( action )
        }
    }

    override suspend fun execute( action: GameAction ): GameAction {
        return when ( action ) {
            is AddPlayer -> this.addPlayer( action )
            is CompleteSignUp -> this.completeSignUp( action )
            is NewGame -> this.newGame( action )
            is RollTheDice -> this.rollTheDice( action )
            is SetBoard -> this.setBoard( action )
            is SetPieces -> this.setPieces( action )
        }
    }

    private suspend fun addPlayer( action: AddPlayer ): AddPlayer {
        return gameState.game().addPlayer( action.playerName, action.playingPiece, action.position )
            .let { this.updateActionForResult( action, it ) }
    }

    private fun completeSignUp( action: CompleteSignUp ): CompleteSignUp {
        this.gameState.game().completeSignUp()
        return this.successfulAction( action )
    }

    private fun newGame( action: NewGame ): NewGame {
        this.gameState.setGame( this.gameFactory.newGame() )
        return this.successfulAction( action )
    }

    private suspend fun rollTheDice( action: RollTheDice, maybeDiceRoll: DiceRoll? = null ): RollTheDice {
        val diceRollResult = gameState.game().rollTheDice( action.position, maybeDiceRoll )
        if ( diceRollResult != null ) {
            action.diceRoll = diceRollResult
            return this.successfulAction( action )
        }
        return this.unsuccessfulAction( action )
    }

    private fun setBoard( action: SetBoard ): SetBoard {
        this.gameState.board = action.board
        return this.successfulAction( action )
    }

    private fun setPieces( action: SetPieces ): SetPieces {
        this.gameState.game().pieceSet = action.pieceSet
        return this.successfulAction( action )
    }

    private fun < T : GameAction > successfulAction( action: T ): T {
        action.executed = true
        action.successful = true
        return action
    }

    private fun < T : GameAction > unsuccessfulAction( action: T ): T {
        action.executed = true
        action.successful = false
        return action
    }

    private fun < T : GameAction > updateActionForResult( action: T, result: Boolean ): T {
        if ( result == true )
            return this.successfulAction( action )
        else
            return this.unsuccessfulAction( action )
    }

}