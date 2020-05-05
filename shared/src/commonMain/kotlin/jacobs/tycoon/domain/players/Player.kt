package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val piece: PlayingPiece,
    val position: Position
) {
    fun isPosition( otherPosition: Position ): Boolean {
        return this.position == otherPosition
    }
}