package jacobs.tycoon.clientcontroller

import jacobs.jsutilities.JsFunction
import jacobs.jsutilities.prototypeFunctions
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.ActualGameController
import jacobs.tycoon.domain.GameController
import jacobs.tycoon.state.GameHistory
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.GameAction
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateSynchroniser ( kodein: Kodein ) {

    private val actualGameStateUpdater by kodein.instance < GameController > ( tag = "actual" )
    private val clientState by kodein.instance < ClientState >()
    private val gameHistory by kodein.instance < GameHistory >()

    private val updateMethods: Map < String, JsFunction > =
        ActualGameController::class.prototypeFunctions().associateBy { it.name }

    fun applyUpdates(stateAction: GameActionCollection) {
        stateAction.getActions().forEach {
            applySingleUpdate( it )
        }
        this.clientState.isWaitingForServer = false
    }

    private fun applySingleUpdate(gameAction: GameAction) {
        getMethod( gameAction )
            .apply( actualGameStateUpdater, gameAction.args() )
        this.gameHistory.logUpdate( gameAction )
    }

    private fun getMethod(gameAction: GameAction): JsFunction {
        if ( false == updateMethods.containsKey( gameAction.methodName ) )
            throw Error( "Unknown update method ${ gameAction.methodName }" )
        return updateMethods.getValue( gameAction.methodName )
    }

}