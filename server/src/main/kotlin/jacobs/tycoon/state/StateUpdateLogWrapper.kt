package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
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
    private val mutex = Mutex()

    private val callList: MutableList < UpdateCall > = mutableListOf()

    // Update API

    fun areThereUpdatesToPush(): Boolean {
        return this.callList.isNotEmpty()
    }

    fun getStateUpdate(): StateUpdate {
        return StateUpdate.fromList( this.callList )
            .also { this.callList.clear() }
    }

    // GameStateUpdater API

    override suspend fun addPlayer( player: Player ) {
        this.callAndLog(
            GameStateUpdater::addPlayer as KFunction2 < GameStateUpdater, Player, Unit >,
            PlayerCall( player )
        )
    }

    override suspend fun setBoard( board: Board ) {
        this.callAndLog(
            GameStateUpdater::setBoard as KFunction2 < GameStateUpdater, Board, Unit >,
            BoardCall( board )
        )
    }

    override suspend fun setPieces( pieces: PieceSet ) {
        this.callAndLog(
            GameStateUpdater::setPieces as KFunction2 < GameStateUpdater, PieceSet, Unit >,
            PieceSetCall( pieces )
        )
    }

    override suspend fun updateStage( newGameStage: GameStage ) {
        this.callAndLog(
            GameStateUpdater::updateStage as KFunction2 < GameStateUpdater, GameStage, Unit >,
            GameStateCall( newGameStage )
        )
    }

    // LOGGING THE CALLS PRIVATE METHODS

    private suspend fun < T : Any > callAndLog( updateFunction: KFunction1 < GameStateUpdater, Unit >,
                                                updateCall: NoArgUpdateCall ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    private suspend fun < T : Any > callAndLog( updateFunction: KFunction2 < GameStateUpdater, T, Unit >,
                                                updateCall: OneArgUpdateCall < T > ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    private suspend fun < T0 : Any, T1 : Any > callAndLog( updateFunction: KFunction3 < GameStateUpdater, T0, T1, Unit >,
                                                updateCall: TwoArgUpdateCall < T0, T1 > ) {
        this.callAndLogUntyped( updateFunction as KFunction < GameStateUpdater >, updateCall )
    }

    /**
     * Only to be called from callAndLog overloaded functions to salvage some type safety
     */
    private suspend fun callAndLogUntyped( updateFunction: KFunction < GameStateUpdater >, updateCall: UpdateCall ) {
        updateCall.functionName = updateFunction.name
        mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
        updateFunction.callSuspend( this.actualStateUpdater, *updateCall.args() )
        this.callList.add( updateCall )
        mutex.unlock()
    }

}