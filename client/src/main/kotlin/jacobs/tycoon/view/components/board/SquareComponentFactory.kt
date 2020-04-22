package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.ActionSquare
import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.board.SquareVisitor
import jacobs.tycoon.domain.board.Station
import jacobs.tycoon.domain.board.Street
import jacobs.tycoon.domain.board.Utility

class SquareComponentFactory() : SquareVisitor <SquareComponent> {

    fun getFromSquare( square: Square ) : SquareComponent {
        return square.accept( this )
    }

    override fun visit( square: ActionSquare ): SquareComponent {
        return ActionSquareComponent(square)
    }

    override fun visit( station: Station ) : StationComponent {
        return StationComponent(station)
    }

    override fun visit( street: Street ) : StreetComponent {
        return StreetComponent(street)
    }

    override fun visit( utility: Utility ) : UtilityComponent {
        return UtilityComponent(utility)
    }

}