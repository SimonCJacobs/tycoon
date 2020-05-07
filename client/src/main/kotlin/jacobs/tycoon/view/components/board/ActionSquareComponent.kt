package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.ActionSquare
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory

class ActionSquareComponent (
    override val square: ActionSquare,
    override val squareController: SquareController
) : SquareComponent < ActionSquare > () {

    override val name = square.name

}