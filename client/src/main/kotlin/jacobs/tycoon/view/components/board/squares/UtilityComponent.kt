package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Utility

class UtilityComponent(
    override val square: Utility,
    override val squareController: SquareController,
    override val squareDisplayStrategy: SquareDisplayStrategy
) : PropertyComponent < Utility > () {

}