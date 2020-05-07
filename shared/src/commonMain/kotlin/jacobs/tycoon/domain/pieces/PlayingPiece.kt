package jacobs.tycoon.domain.pieces

import jacobs.tycoon.domain.board.Square
import kotlinx.serialization.Serializable

@Serializable
class PlayingPiece(
    val name: String,
    var square: Square = Square.NULL
) {
    companion object {
        val NULL = PlayingPiece( "" )
    }

    fun moveToSquare( newSquare: Square ) {
        this.square = newSquare
    }

    override fun equals( other: Any? ): Boolean {
        if ( other is PlayingPiece )
            return this.name == other.name
        else
            return false
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

}