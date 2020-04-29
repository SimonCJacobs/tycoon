package jacobs.tycoon.servercontroller

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class ServerMainController ( kodein: Kodein ) {

    private val game by kodein.instance < Game > ()
    private val state by kodein.instance < GameState > ()

    fun addPlayer( name: String, playingPiece: PlayingPiece ): Boolean {
        val newPlayer = this.game.addPlayer( name, playingPiece )
        this.state.players.addPlayer( newPlayer )
        return true
    }

    fun getAvailablePieces(): PlayingPieceList {
        return this.game.getAvailablePieces(
            this.state.pieceSet,
            this.state.players
        )
    }

    fun getGameStage(): GameStage {
        return this.state.stage
    }

}