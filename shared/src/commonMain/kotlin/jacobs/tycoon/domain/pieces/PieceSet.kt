package jacobs.tycoon.domain.pieces

import jacobs.tycoon.domain.players.GamePlayers
import kotlinx.serialization.Serializable

@Serializable
abstract class PieceSet {

    abstract val name: String
    protected abstract val pieces: List < PlayingPiece >

    fun count(): Int {
        return this.pieces.size
    }

    fun getAvailablePieces( players: GamePlayers ): List < PlayingPiece > {
        val piecesInUse = players.getPiecesInUse()
        return this.pieces.filter {
            false == piecesInUse.contains( it )
        }
    }

}