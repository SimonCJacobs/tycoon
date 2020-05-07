package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class NullSquare : Square() {

    override val indexOnBoard = -1
    override val name: String = ""

    override fun < T > accept( squareVisitor: SquareVisitor<T>): T {
        throw Error( "NullSquare cannot be visited" )
    }

}
