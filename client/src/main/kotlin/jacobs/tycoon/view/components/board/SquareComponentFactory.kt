package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.ActionSquare
import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.board.SquareVisitor
import jacobs.tycoon.domain.board.Station
import jacobs.tycoon.domain.board.Street
import jacobs.tycoon.domain.board.Utility
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class SquareComponentFactory( kodein: Kodein ) : SquareVisitor < SquareComponent < * > > {

    private val squareController by kodein.instance < SquareController > ()

    fun getFromSquare( square: Square ): SquareComponent < * > {
        return square.accept( this )
    }

    override fun visit( square: ActionSquare ): ActionSquareComponent {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( station: Station ): StationComponent {
        return StationComponent( station, this.squareController )
    }

    override fun visit( street: Street ): StreetComponent {
        return StreetComponent( street, this.squareController )
    }

    override fun visit( utility: Utility ): UtilityComponent {
        return UtilityComponent( utility, this.squareController )
    }

}