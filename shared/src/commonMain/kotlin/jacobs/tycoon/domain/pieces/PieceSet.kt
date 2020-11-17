package jacobs.tycoon.domain.pieces

import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.players.GamePlayers
import kotlinx.serialization.Serializable

@Serializable
abstract class PieceSet {

    abstract val name: String
    protected abstract val pieces: List < PlayingPiece >
    private var piecesInUse: List < PlayingPiece > = emptyList()

    fun count(): Int {
        return this.pieces.size
    }

    fun freezePiecesInUse( players: GamePlayers ) {
        this.piecesInUse = players.activeOrderedList().map { eachPlayer -> eachPlayer.piece }
    }

    fun getAvailablePieces( players: GamePlayers ): List < PlayingPiece > {
        val piecesInUse = players.getPiecesInUse()
        return this.pieces.filter {
            false == piecesInUse.contains( it )
        }
    }

    fun getPieceByName( name: String ): PlayingPiece? {
        return this.pieces.firstOrNull { it.name == name }
    }

    fun getPiecesOnSquare( square: Square): Set < PlayingPiece > {
        return this.piecesInUse.filter { it.square == square }.toSet()
    }

    fun moveAllToSquare( square: Square) {
        this.piecesInUse.forEach { it.moveToSquare( square ) }
    }

}