package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.NoArgAction
import jacobs.tycoon.domain.actions.OneArgAction
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.actions.TwoArgAction
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.sync.Mutex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.full.callSuspend

@Suppress( "UNCHECKED_CAST" )
class GameControllerWrapper( kodein: Kodein ) : GameController {

    private val actualStateUpdater by kodein.instance < GameController > ( tag = "actual" )
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    // GameStateUpdater API

    override suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.callAndLogOnSuccess(
            GameController::addPlayer as KFunction3 < GameController, String, PlayingPiece, Boolean >,
            AddPlayer( playerName, playingPiece )
        )
    }

    override suspend fun completeSignUp(): Boolean {
        return this.callAndLogOnSuccess(
            GameController::completeSignUp as KFunction1 < GameController, Boolean >,
            CompleteSignUp
        )
    }

    override suspend fun newGame(): Boolean {
        return this.callAndLogOnSuccess(
            GameController::newGame as KFunction1 < GameController, Boolean >,
            NewGame
        )
    }

    override suspend fun setBoard( board: Board ): Boolean {
        return this.callAndLogOnSuccess(
            GameController::setBoard as KFunction2 < GameController, Board, Boolean >,
            SetBoard( board )
        )
    }

    override suspend fun setPieces( pieces: PieceSet ): Boolean {
        return this.callAndLogOnSuccess(
            GameController::setPieces as KFunction2 < GameController, PieceSet, Boolean >,
            SetPieces( pieces )
        )
    }

    // LOGGING THE CALLS PRIVATE METHODS

    private suspend fun callAndLogOnSuccess( updateFunction: KFunction1 < GameController, Boolean >,
                                             actionCall: NoArgAction ): Boolean {
        return this.callAndLogOnSuccessUntyped( updateFunction as KFunction < Boolean>, actionCall )
    }

    private suspend fun < T : Any > callAndLogOnSuccess( updateFunction: KFunction2 < GameController, T, Boolean >,
                                                         actionCall: OneArgAction < T > ): Boolean {
        return this.callAndLogOnSuccessUntyped( updateFunction as KFunction < Boolean >, actionCall )
    }

    private suspend fun < T0 : Any, T1 : Any > callAndLogOnSuccess(
            updateFunction: KFunction3 < GameController, T0, T1, Boolean >,
            actionCall: TwoArgAction< T0, T1 > ): Boolean {
        return this.callAndLogOnSuccessUntyped( updateFunction as KFunction < Boolean >, actionCall )
    }

    /**
     * Only to be called from callAndLog overloaded functions to salvage some type safety
     */
    private suspend fun callAndLogOnSuccessUntyped( updateFunction: KFunction < Boolean >,
                                                      gameAction: GameAction ): Boolean {
        if ( updateFunction.name != gameAction.methodName )
            throw Error( "Update method mismatch: Method ${ updateFunction.name } chosen for update ${ gameAction.methodName }")
        mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
        val wasSuccess = updateFunction.callSuspend( this.actualStateUpdater, *gameAction.args() )
        if ( wasSuccess ) this.gameHistory.logUpdate( gameAction )
        mutex.unlock()
        return wasSuccess
    }

}