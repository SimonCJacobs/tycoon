package jacobs.tycoon.clientcontroller

import jacobs.jsutilities.JsFunction
import jacobs.jsutilities.prototypeFunctions
import jacobs.mithril.Mithril
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.ActualGameController
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.state.GameHistory
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.GameAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateSynchroniser ( kodein: Kodein ) {

    private val actualGameStateUpdater by kodein.instance < GameController > ( tag = "actual" )
    private val clientState by kodein.instance < ClientState >()
    private val gameHistory by kodein.instance < GameHistory >()
    private val mithril = Mithril()

    private val updateMethods: Map < String, JsFunction > =
        ActualGameController::class.prototypeFunctions().associateBy { it.name }

    suspend fun applyUpdates( stateAction: GameActionCollection ) {
        stateAction.getActions().forEach {
            applySingleUpdate( it )
        }
        this.clientState.isWaitingForServer = false
        mithril.redraw()
    }

    private suspend fun applySingleUpdate( gameAction: GameAction ) {
        this.rerunGameActionClientSide( gameAction )
        gameHistory.logUpdate( gameAction )
    }

    private suspend fun rerunGameActionClientSide( gameAction: GameAction ) {
        val result = getMethod( gameAction )
                .apply( actualGameStateUpdater, gameAction.args() )
            // The GameController may return a Deferred. We must wait for it in order to ensure
            // that the game screen is only redrawn once all updates are dealt with (ie keeping the code
            // synchronous)
        if ( result is Deferred < * > )
            result.await()
    }

    private fun getMethod(gameAction: GameAction): JsFunction {
        if ( false == updateMethods.containsKey( gameAction.methodName ) )
            throw Error( "Unknown update method ${ gameAction.methodName }" )
        return updateMethods.getValue( gameAction.methodName )
    }

}