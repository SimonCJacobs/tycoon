package jacobs.tycoon.domain.pieces

import kotlinx.serialization.Serializable

@Serializable
class PlayingPiece(
    val name: String
) {
    companion object {
        val NULL = PlayingPiece( "" )
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