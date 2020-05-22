package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.ActionSquare

class ActionSquareComponent (
    override val square: ActionSquare,
    override val squareController: SquareController,
    override val squareDisplayStrategy: SquareDisplayStrategy
) : SquareComponent < ActionSquare > ()