package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class FreeParkingSquare(
    override val indexOnBoard: Int,
    override val name: String
) : ActionSquare() {
    
    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}