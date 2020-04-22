package jacobs.tycoon.domain.pieces

class ClassicPieces : PieceSet() {

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