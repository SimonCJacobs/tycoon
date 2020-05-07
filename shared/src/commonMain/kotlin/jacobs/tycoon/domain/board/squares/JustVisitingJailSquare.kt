package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class JustVisitingJailSquare (
    override val indexOnBoard: Int
) : ActionSquare() {

    override val name: String = "Jail, just visiting"
    
    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

}