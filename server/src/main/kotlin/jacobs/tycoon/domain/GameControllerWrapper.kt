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
import jacobs.tycoon.domain.actions.ThreeArgAction
import jacobs.tycoon.domain.actions.TwoArgAction
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Position
import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.KFunction4
import kotlin.reflect.full.callSuspend

@Suppress( "UNCHECKED_CAST" )
class GameControllerWrapper( kodein: Kodein ) : GameController {

    private val actualStateUpdater by kodein.instance < GameController > ( tag = "actual" )
    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    // GameStateUpdater API

    override fun addPlayerAsync( playerName: String, playingPiece: PlayingPiece,
                                 position: Position ): Deferred < Boolean > {
        return this.callAndLogOnSuccessAsync(
            GameController::addPlayerAsync,
            AddPlayer( playerName, playingPiece, position )
        )
    }

    override fun completeSignUpAsync(): Deferred < Boolean > {
        return this.callAndLogOnSuccessAsync(
            GameController::completeSignUpAsync,
            CompleteSignUp
        )
    }

    override fun newGameAsync(): Deferred < Boolean > {
        return this.callAndLogOnSuccessAsync(
            GameController::newGameAsync,
            NewGame
        )
    }

    override fun setBoardAsync( board: Board ): Deferred < Boolean > {
        return this.callAndLogOnSuccessAsync(
            GameController::setBoardAsync,
            SetBoard( board )
        )
    }

    override fun setPiecesAsync( pieces: PieceSet ): Deferred < Boolean > {
        return this.callAndLogOnSuccessAsync(
            GameController::setPiecesAsync,
            SetPieces( pieces )
        )
    }

    // LOGGING THE CALLS PRIVATE METHODS

    private fun callAndLogOnSuccessAsync( updateFunction: KFunction1 < GameController, Deferred < Boolean > >,
                                         actionCall: NoArgAction ): Deferred < Boolean > {
        return this.callAndLogOnSuccessUntypedAsync( updateFunction as KFunction < Deferred < Boolean > >, actionCall )
    }

    private fun < T : Any > callAndLogOnSuccessAsync( updateFunction: KFunction2 < GameController, T, Deferred < Boolean > >,
                                                     actionCall: OneArgAction < T > ): Deferred < Boolean > {
        return this.callAndLogOnSuccessUntypedAsync( updateFunction as KFunction < Deferred < Boolean > >, actionCall )
    }

    private fun < T0 : Any, T1 : Any > callAndLogOnSuccessAsync(
            updateFunction: KFunction3 < GameController, T0, T1, Deferred < Boolean > >,
            actionCall: TwoArgAction< T0, T1 > ): Deferred < Boolean > {
        return this.callAndLogOnSuccessUntypedAsync( updateFunction as KFunction < Deferred < Boolean > >, actionCall )
    }

    private fun < T0 : Any, T1 : Any, T2: Any > callAndLogOnSuccessAsync(
        updateFunction: KFunction4 < GameController, T0, T1, T2, Deferred < Boolean > >,
        actionCall: ThreeArgAction < T0, T1, T2 > ): Deferred < Boolean > {
        return this.callAndLogOnSuccessUntypedAsync( updateFunction as KFunction < Deferred < Boolean > >, actionCall )
    }

    /**
     * Only to be called from callAndLog overloaded functions to salvage some type safety
     */
    private fun callAndLogOnSuccessUntypedAsync( updateFunction: KFunction < Deferred < Boolean > >,
                                                gameAction: GameAction ): Deferred < Boolean > {
        if ( updateFunction.name != gameAction.methodName )
            throw Error( "Update method mismatch: Method ${ updateFunction.name } chosen for update ${ gameAction.methodName }")
        return this.coroutineScope.async{
            mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
            val wasSuccess = updateFunction.callSuspend( actualStateUpdater, *gameAction.args() ).await()
            if ( wasSuccess ) gameHistory.logUpdate( gameAction )
            mutex.unlock()
            wasSuccess
        }
    }

}