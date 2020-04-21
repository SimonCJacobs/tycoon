package jacobs.tycoon.view.components

import jacobs.mithril.HyperScriptFactory
import jacobs.tycoon.domain.ActionSquare
import jacobs.tycoon.domain.Square
import jacobs.tycoon.domain.SquareVisitor
import jacobs.tycoon.domain.Station
import jacobs.tycoon.domain.Street
import jacobs.tycoon.domain.Utility

class SquareComponentFactory(
    private val m: HyperScriptFactory
) : SquareVisitor < SquareComponent > {

    fun getFromSquare( square: Square ) : SquareComponent {
        return square.accept( this )
    }

    override fun visit( square: ActionSquare ): SquareComponent {
        return ActionSquareComponent( m, square )
    }

    override fun visit( station: Station ) : StationComponent {
        return StationComponent( m, station )
    }

    override fun visit( street: Street ) : StreetComponent {
        return StreetComponent( m, street )
    }

    override fun visit( utility: Utility ) : UtilityComponent {
        return UtilityComponent( m, utility )
    }

}