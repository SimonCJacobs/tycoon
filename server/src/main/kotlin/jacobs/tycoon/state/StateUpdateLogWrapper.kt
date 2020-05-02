package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.Player
import kotlinx.coroutines.sync.Mutex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.full.callSuspend

@Suppress( "UNCHECKED_CAST" )
class StateUpdateLogWrapper( kodein: Kodein ) : GameStateUpdater {

    private val actualStateUpdater by kodein.instance < ActualGameStateUpdater > ()
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    // GameStateUpdater API

    override suspend fun addPlayer( player: Player ) {
        this.callAndLog(
            GameStateUpdater::addPlayer as KFunction2 < GameStateUpdater, Player, Unit >,
            AddPlayer( player )
        )
    }

    override suspend fun setBoard( board: Board ) {
        this.callAndLog(
            GameStateUpdater::setBoard as KFunction2 < GameStateUpdater, Board, Unit >,
            SetBoard( board )
        )
    }

    override suspend fun setPieces( pieces: PieceSet ) {
        this.callAndLog(
            GameStateUpdater::setPieces as KFunction2 < GameStateUpdater, PieceSet, Unit >,
            SetPieces( pieces )
        )
    }

    override suspend fun updateStage( newGamePhase: GamePhase ) {
        this.callAndLog(
            GameStateUpdater::updateStage as KFunction2 < GameStateUpdater, GamePhase, Unit >,
            UpdateStage( newGamePhase )
        )
    }

    // LOGGING THE CALLS PRIVATE METHODS

    private suspend fun < T : Any > callAndLog( updateFunction: KFunction1 < GameStateUpdater, Unit >,
                                                updateCall: NoArgUpdate ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    private suspend fun < T : Any > callAndLog( updateFunction: KFunction2 < GameStateUpdater, T, Unit >,
                                                updateCall: OneArgUpdate < T > ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    private suspend fun < T0 : Any, T1 : Any > callAndLog( updateFunction: KFunction3 < GameStateUpdater, T0, T1, Unit >,
                                                updateCall: TwoArgUpdate < T0, T1 > ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    /**
     * Only to be called from callAndLog overloaded functions to salvage some type safety
     */
    private suspend fun callAndLogUntyped( updateFunction: KFunction < GameStateUpdater >, gameUpdate: GameUpdate ) {
        if ( updateFunction.name != gameUpdate.methodName )
            throw Error( "Update method mismatch: Method ${ updateFunction.name } chosen for update ${ gameUpdate.methodName }")
        mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
        updateFunction.callSuspend( this.actualStateUpdater, *gameUpdate.args() )
        this.gameHistory.logUpdate( gameUpdate )
        mutex.unlock()
    }

}