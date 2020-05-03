package jacobs.tycoon.application

import jacobs.tycoon.controller.communication.ClientWelcomeMessage
import jacobs.tycoon.state.GameHistory
import jacobs.websockets.SocketId
import kotlinx.coroutines.delay
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class UpdateEngine ( kodein: Kodein ) {

    private val gameHistory by kodein.instance < GameHistory > ()
    private val socketServer by kodein.instance < SocketServer > ()
    private val updateDelayMs by kodein.instance < Long > ( tag = "updateDelay" )

    private val updatePushRecord: MutableMap < SocketId, Int > = mutableMapOf()

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
    fun updateNewConnection( socket: SocketId ) {
        this.sendWelcomeMessage( socket )
        this.updatePushRecord.put( socket, 0 )
    }

    private fun doAnyOngoingUpdates() {
        val updateCount = this.gameHistory.getUpdateCount()
        this.updatePushRecord.forEach {
            if ( it.value < updateCount )
                updateSocketInIndexRange( it.key, it.value, updateCount )
        }
    }

    private fun sendWelcomeMessage( socket: SocketId ) {
        val welcomeMessage = ClientWelcomeMessage( "Welcome to the game :)", socket )
        this.socketServer.notifySocketAtIndex( socket, welcomeMessage )
    }

    private fun updateSocketInIndexRange( socket: SocketId, firstIndexToUpdate: Int, lastIndexToUpdate: Int ) {
        this.socketServer.notifySocketAtIndex(
            socket,
            this.gameHistory.getUpdatesBetween( firstIndexToUpdate, lastIndexToUpdate )
        )
        this.updatePushRecord [ socket ] = lastIndexToUpdate
    }

}