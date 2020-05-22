package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.JailSquare

open class JailComponent(
    override val square: JailSquare,
    override val squareController: SquareController,
    override val squareDisplayStrategy: SquareDisplayStrategy,
    squaresToASideExcludingCorners: Int
) : SquareComponent<JailSquare>() {

    override var tableColumnCount = squaresToASideExcludingCorners

}
