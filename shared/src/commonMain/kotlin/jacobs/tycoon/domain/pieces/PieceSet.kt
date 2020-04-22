package jacobs.tycoon.domain.pieces

import jacobs.tycoon.domain.players.GamePlayers

abstract class PieceSet {

    protected abstract val pieces: List < PlayingPiece >

    fun getAvailablePieces( players: GamePlayers ): List < PlayingPiece > {
        val piecesInUse = players.getPiecesInUse()
        return this.pieces.filter {
            false == piecesInUse.contains( it )
        }
    }

}