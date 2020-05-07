package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Street

class StreetComponent(
    override val square: Street,
    override val squareController: SquareController
) : PropertyComponent <Street> () {

    override val name = square.name

}