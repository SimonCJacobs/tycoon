package jacobs.tycoon.domain.pieces

class PlayingPiece(
    val name: String
) {

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