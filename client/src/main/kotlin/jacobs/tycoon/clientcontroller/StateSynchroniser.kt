package jacobs.tycoon.clientcontroller

import jacobs.jsutilities.JsFunction
import jacobs.jsutilities.prototypeFunctions
import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.state.ActualGameStateUpdater
import jacobs.tycoon.state.GameState
import jacobs.tycoon.state.GameStateUpdater
import jacobs.tycoon.state.StateUpdate
import jacobs.tycoon.state.UpdateCall
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateSynchroniser ( kodein: Kodein ) {

    private val actualGameStateUpdater by kodein.instance < ActualGameStateUpdater > ()
    private val clientState by kodein.instance < ClientState >()

    private var hasReceivedInitialUpdate = false
    private val updateMethods: Map < String, JsFunction > =
        ActualGameStateUpdater::class.prototypeFunctions().associateBy { it.name }


    fun applyUpdates( stateUpdate: StateUpdate) {
        stateUpdate.getUpdates().forEach {
            applySingleUpdate( it )
        }
        if ( false == hasReceivedInitialUpdate ) {
            console.log( "first update in chaps" )
            this.hasReceivedInitialUpdate = true
            this.clientState.isWaitingForServer = false
        }
    }

    private fun applySingleUpdate( updateCall: UpdateCall ) {
        getMethod( updateCall )
            .apply( actualGameStateUpdater, updateCall.args() )
    }

    private fun getMethod( updateCall: UpdateCall): JsFunction {
        if ( false == updateMethods.containsKey( updateCall.functionName ) )
            throw Error( "Unknown method ${ updateCall.functionName }" )
        return updateMethods.getValue( updateCall.functionName )
    }

}