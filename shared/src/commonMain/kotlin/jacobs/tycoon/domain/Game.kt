package jacobs.tycoon.domain

import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState

class Game {

    fun addPlayer( name: String, piece: PlayingPiece ): Player {
        return Player( name, piece )
    }

    fun canGameStart( gameState: GameState ): Boolean {
        return gameState.players.count() >= gameState.minimumNumberOfPlayers
    }

    fun getAvailablePieces( pieceSet: PieceSet, players: GamePlayers ): List < PlayingPiece > {
        return pieceSet.getAvailablePieces( players )
    }

}