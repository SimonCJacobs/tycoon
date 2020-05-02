package jacobs.tycoon.application

import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class UpdateEngine ( kodein: Kodein ) {

    private val gameHistory by kodein.instance < GameHistory > ()
    private val socketServer by kodein.instance < SocketServer > ()
    private val updateDelayMs by kodein.instance < Long > ( tag = "updateDelay" )

    private val updatePushRecord: MutableMap < Int, Int > = mutableMapOf()

    init {
        socketServer.setNewConnectionLambda { this.updateNewConnection( it ) }
    }

    suspend fun startUpdating() {
        while ( true ) {
            this.doAnyOngoingUpdates()
            delay( updateDelayMs )
        }
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun updateNewConnection( index: Int ) {
        this.updatePushRecord.put( index, 0 )
    }

    private fun doAnyOngoingUpdates() {
        val updateCount = this.gameHistory.getUpdateCount()
        this.updatePushRecord.forEach {
            if ( it.value < updateCount )
                updateSocketInIndexRange( it.key, it.value, updateCount )
        }
    }

    private fun updateSocketInIndexRange( socketIndex: Int, firstIndexToUpdate: Int, lastIndexToUpdate: Int ) {
        this.socketServer.notifySocketAtIndex(
            socketIndex,
            this.gameHistory.getUpdatesBetween( firstIndexToUpdate, lastIndexToUpdate )
        )
        this.updatePushRecord [ socketIndex ] = lastIndexToUpdate
    }

}