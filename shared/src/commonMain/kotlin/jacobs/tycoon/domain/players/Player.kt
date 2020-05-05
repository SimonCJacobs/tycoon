package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val piece: PlayingPiece,
    val position: Position
) : Comparable < Player > {

    companion object {
        val NULL = Player( "", PlayingPiece.NULL, Position.UNINITIALISED )
    }

    fun isPosition( otherPosition: Position ): Boolean {
        return this.position == otherPosition
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }
}