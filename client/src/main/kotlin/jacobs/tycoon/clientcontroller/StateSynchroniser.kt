package jacobs.tycoon.clientcontroller

import jacobs.jsutilities.JsFunction
import jacobs.jsutilities.prototypeFunctions
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.state.ActualGameStateUpdater
import jacobs.tycoon.state.GameHistory
import jacobs.tycoon.state.GameUpdateCollection
import jacobs.tycoon.state.GameUpdate
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateSynchroniser ( kodein: Kodein ) {

    private val actualGameStateUpdater by kodein.instance < ActualGameStateUpdater > ()
    private val clientState by kodein.instance < ClientState >()
    private val gameHistory by kodein.instance < GameHistory >()

    private val updateMethods: Map < String, JsFunction > =
        ActualGameStateUpdater::class.prototypeFunctions().associateBy { it.name }

    fun applyUpdates( stateUpdate: GameUpdateCollection ) {
        stateUpdate.getUpdates().forEach {
            applySingleUpdate( it )
        }
        this.clientState.isWaitingForServer = false
    }

    private fun applySingleUpdate( gameUpdate: GameUpdate ) {
        getMethod( gameUpdate )
            .apply( actualGameStateUpdater, gameUpdate.args() )
        this.gameHistory.logUpdate( gameUpdate )
    }

    private fun getMethod(gameUpdate: GameUpdate): JsFunction {
        if ( false == updateMethods.containsKey( gameUpdate.methodName ) )
            throw Error( "Unknown update method ${ gameUpdate.methodName }" )
        return updateMethods.getValue( gameUpdate.methodName )
    }

}