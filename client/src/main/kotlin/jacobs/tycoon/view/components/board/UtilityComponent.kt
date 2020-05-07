package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Utility

class UtilityComponent(
    override val square: Utility,
    override val squareController: SquareController
) : PropertyComponent <Utility>() {

    override val name = square.name

}