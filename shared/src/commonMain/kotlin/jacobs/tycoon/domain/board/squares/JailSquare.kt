package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class JailSquare : ActionSquare() {

    override val indexOnBoard: Int = -1 // Should not be used
    override val name: String = "Jail"

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }
}