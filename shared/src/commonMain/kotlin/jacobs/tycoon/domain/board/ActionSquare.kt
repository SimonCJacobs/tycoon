package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class ActionSquare : Square() {

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}