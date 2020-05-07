package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.ActionSquare

class ActionSquareComponent (
    override val square: ActionSquare,
    override val squareController: SquareController
) : SquareComponent < ActionSquare > () {

    override val name = square.name

}