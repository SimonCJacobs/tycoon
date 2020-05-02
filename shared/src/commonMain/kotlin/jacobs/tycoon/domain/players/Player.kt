package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
class Player(
    val name: String,
    val piece: PlayingPiece
) {

}
