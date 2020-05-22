package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Street

class StreetComponent(
    override val square: Street,
    override val squareController: SquareController,
    override val squareDisplayStrategy: SquareDisplayStrategy
) : PropertyComponent < Street > ()