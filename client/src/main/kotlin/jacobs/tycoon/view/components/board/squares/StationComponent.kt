package jacobs.tycoon.view.components.board.squares

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Station

class StationComponent(
    override val square: Station,
    override val squareController: SquareController,
    override val squareDisplayStrategy: SquareDisplayStrategy
) : PropertyComponent < Station > ()