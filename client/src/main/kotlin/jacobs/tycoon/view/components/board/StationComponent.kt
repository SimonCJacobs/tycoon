package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.Station
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory

class StationComponent(
    override val square: Station,
    override val squareController: SquareController
) : PropertyComponent < Station > () {

    override val name = square.name

}