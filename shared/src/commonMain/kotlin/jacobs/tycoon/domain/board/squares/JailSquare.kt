package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class JailSquare (
    override val indexOnBoard: Int
) : ActionSquare() {

    companion object {
        val NULL = JailSquare( 0 )
    }

    override val name: String = "Jail"

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }
}