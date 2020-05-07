package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
class GoToJailSquare(
    override val indexOnBoard: Int
) : ActionSquare() {

    override val name: String = "Go To Jail"
    
    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }
    
}