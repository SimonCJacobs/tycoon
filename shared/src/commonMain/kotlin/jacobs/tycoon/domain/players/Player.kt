package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.websockets.SocketId
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val piece: PlayingPiece,
    val position: Position
)
