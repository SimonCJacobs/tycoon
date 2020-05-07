package jacobs.tycoon.view.components.board

import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.ActionSquare
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.FreeParkingSquare
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.GoToJailSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.JustVisitingJailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.SquareVisitor
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.TaxSquare
import jacobs.tycoon.domain.board.squares.Utility
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class SquareComponentFactory( kodein: Kodein ) : SquareVisitor<SquareComponent<*>> {

    private val squareController by kodein.instance < SquareController > ()

    fun getFromSquare( square: Square ): SquareComponent < * > {
        return square.accept( this )
    }

    override fun visit( square: CardSquare ): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( square: FreeParkingSquare ): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( square: GoSquare ): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( square: GoToJailSquare): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( square: JailSquare ): SquareComponent<*> {
        throw Error( "There is no unique jail square" )
    }

    override fun visit( square: JustVisitingJailSquare): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

    override fun visit( square: Station): StationComponent {
        return StationComponent( square, this.squareController )
    }

    override fun visit( square: Street ): StreetComponent {
        return StreetComponent( square, this.squareController )
    }

    override fun visit( square: Utility): UtilityComponent {
        return UtilityComponent( square, this.squareController )
    }

    override fun visit( square: TaxSquare): SquareComponent<*> {
        return ActionSquareComponent( square, this.squareController )
    }

}