package jacobs.tycoon.services

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.controller.communication.toPosition
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import jacobs.websockets.SocketId
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerIdentifier ( kodein: Kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()

    fun getPlayerFromSeatingPosition( seatingPosition: SeatingPosition ): Player {
        return gameState.game().players.getPlayerAtPosition( seatingPosition )
    }

    fun isPlayerUsingThisMachine(testPlayer: Player ): Boolean {
        return clientState.maybeSocket
            ?.let { this.getPlayerFromSocketId( it ) == testPlayer }
            ?: false
    }

    fun hasUserOfThisMachineSignedUpForGame(): Boolean {
        return clientState.maybeSocket
            ?.let { gameState.game().players.hasPlayerInPositionSignedUp( it.toPosition() ) }
            ?: false
    }

    fun < T > mapOtherPlayers( callback: ( Player ) -> T ): List < T > {
        return this.gameState.game().players.playersToLeftExcluding( playerUsingThisMachine )
            .map( callback )
    }

    val playerUsingThisMachine: Player
        get() = this.getPlayerFromSocketId( clientState.socketNotNull )

    private fun getPlayerFromSocketId(socketId: SocketId ): Player {
        return this.getPlayerFromSeatingPosition(
            socketId.toPosition()
        )
    }

}