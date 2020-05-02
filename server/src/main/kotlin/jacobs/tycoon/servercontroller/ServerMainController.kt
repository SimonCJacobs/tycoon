package jacobs.tycoon.servercontroller

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import jacobs.tycoon.state.StateUpdateLogWrapper
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class ServerMainController ( kodein: Kodein ) {

    private val game by kodein.instance < Game > ()
    private val stateUpdater by kodein.instance < StateUpdateLogWrapper > ()

    suspend fun addPlayer( name: String, playingPiece: PlayingPiece ): Boolean {
        val newPlayer = this.game.addPlayer( name, playingPiece )
        this.stateUpdater.addPlayer( newPlayer )
        return true
    }

}