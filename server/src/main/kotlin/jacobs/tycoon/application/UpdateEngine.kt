package jacobs.tycoon.application

import jacobs.tycoon.state.StateUpdate
import jacobs.tycoon.state.StateUpdateLogWrapper
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class UpdateEngine ( kodein: Kodein ) {

    private val socketServer by kodein.instance < SocketServer > ()
    private val stateUpdater by kodein.instance < StateUpdateLogWrapper > ()
    private val updateDelayMs by kodein.instance < Long > ( tag = "updateDelay" )

    private val updateHistory: StateUpdate = StateUpdate.empty()

    init {
        socketServer.setNewConnectionLambda { this.updateNewConnection( it ) }
    }

    suspend fun startUpdating() {
        this.doAnyFirstTimeUpdates()
        while ( true ) {
            this.doAnyOngoingUpdates()
            delay( updateDelayMs )
        }
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun updateNewConnection(index: Int ) {
        socketServer.notifySocketOfIndex( index, this.updateHistory )
    }

    private fun doAnyFirstTimeUpdates() {
        if ( false == this.updateHistory.isEmpty() ) {
            this.pushUpdateToAll( this.updateHistory )
        }
    }

    private fun doAnyOngoingUpdates() {
        if ( false == this.stateUpdater.areThereUpdatesToPush() )
            return
        val currentUpdates = this.stateUpdater.getStateUpdate()
        updateHistory.combine( currentUpdates )
        this.pushUpdateToAll( currentUpdates )
        println( "done an ongoing update")
    }

    private fun pushUpdateToAll( update: StateUpdate ) {
        socketServer.notifyAllSockets( update )
    }

}