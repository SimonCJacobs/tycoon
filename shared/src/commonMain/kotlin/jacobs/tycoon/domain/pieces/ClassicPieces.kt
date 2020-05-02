package jacobs.tycoon.domain.pieces

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ClassicPieces : PieceSet() {

    @Transient
    override val name = "Classic"

    @Transient
    override val pieces: List < PlayingPiece > = listOf(
        PlayingPiece( "Battleship" ),
        PlayingPiece( "Boot" ),
        PlayingPiece( "Cannon" ),
        PlayingPiece( "Horse and rider" ),
        PlayingPiece( "Iron" ),
        PlayingPiece( "Racing car" ),
        PlayingPiece( "Scottie dog" ),
        PlayingPiece( "Thimble" ),
        PlayingPiece( "Top hat" ),
        PlayingPiece( "Wheelbarrow" )
    )

}