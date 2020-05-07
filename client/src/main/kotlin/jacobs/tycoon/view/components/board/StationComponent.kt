package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Station

class StationComponent(
    override val square: Station,
    override val squareController: SquareController
) : PropertyComponent <Station> () {

    override val name = square.name

}