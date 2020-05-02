package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class ActionSquare( override val name: String ) : Square() {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}