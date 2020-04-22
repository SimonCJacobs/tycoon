package jacobs.tycoon.view.components

import jacobs.mithril.m
import jacobs.tycoon.domain.ActionSquare
import jacobs.tycoon.domain.Square
import jacobs.tycoon.domain.SquareVisitor
import jacobs.tycoon.domain.Station
import jacobs.tycoon.domain.Street
import jacobs.tycoon.domain.Utility

class SquareComponentFactory() : SquareVisitor < SquareComponent > {

    fun getFromSquare( square: Square ) : SquareComponent {
        return square.accept( this )
    }

    override fun visit( square: ActionSquare ): SquareComponent {
        return ActionSquareComponent( square )
    }

    override fun visit( station: Station ) : StationComponent {
        return StationComponent( station )
    }

    override fun visit( street: Street ) : StreetComponent {
        return StreetComponent( street )
    }

    override fun visit( utility: Utility ) : UtilityComponent {
        return UtilityComponent( utility )
    }

}