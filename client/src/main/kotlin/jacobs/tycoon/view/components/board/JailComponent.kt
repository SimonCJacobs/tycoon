package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.JailSquare

class JailComponent(
    override val square: JailSquare,
    override val squareController: SquareController,
    squaresToASideExcludingCorners: Int
) : SquareComponent < JailSquare >() {

    override val name = "Jail"
    override var tableColumnCount = squaresToASideExcludingCorners

}
