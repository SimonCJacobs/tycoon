package jacobs.tycoon.view.components.board.squares

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

class SquareComponentRepository(
    kodein: Kodein
) : SquareVisitor <SquareComponent<*>> {

    private val squareController by kodein.instance < SquareController > ()

    private val normalDisplayComponents: SquareComponentCollection = SquareComponentCollection()

    fun getFromSquare( square: Square ): SquareComponent<*> {
        return square.accept( this )
    }

    fun getForJailSquare( jailSquare: JailSquare, squaresToASideExcludingCorners: Int ): JailComponent {
    //    if ( null == this.collection().jailComponent )
      //      this.collection().jailComponent =
                return JailComponent(
                square = jailSquare,
                squareController = squareController,
                squareDisplayStrategy = squareController.getSquareDisplayStrategy( jailSquare ),
                squaresToASideExcludingCorners = squaresToASideExcludingCorners
            )
     //   return this.collection().jailComponent!!
    }

    override fun visit( square: CardSquare ): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    override fun visit( square: FreeParkingSquare ): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    override fun visit( square: GoSquare ): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    override fun visit( square: GoToJailSquare): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    override fun visit( square: JailSquare ): SquareComponent<*> {
        throw Error( "Jail not to be constructed by visitation" )
    }

    override fun visit( square: JustVisitingJailSquare ): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    override fun visit( square: Station ): SquareComponent<*> {
     //   if ( false == this.collection().stations.contains( square ) )
            return StationComponent (
                square = square,
                squareController = this.squareController,
                squareDisplayStrategy = squareController.getSquareDisplayStrategy( square )
            )
       //         .let { this.collection().stations.put( square, it ) }
      //  return this.collection().stations.getValue( square )
    }

    override fun visit( square: Street ): SquareComponent<*> {
     //   if ( false == this.collection().streets.contains( square ) )
      return      StreetComponent (
                square = square,
                squareController = this.squareController,
                squareDisplayStrategy = squareController.getSquareDisplayStrategy( square )
            )
     //           .let { this.collection().streets.put( square, it ) }
     //   return this.collection().streets.getValue( square )
    }

    override fun visit( square: Utility ): SquareComponent<*> {
     //   if ( false == this.collection().utilities.contains( square ) )
     return       UtilityComponent (
                square = square,
                squareController = this.squareController,
                squareDisplayStrategy = squareController.getSquareDisplayStrategy( square )
            )
     //          .let { this.collection().utilities.put( square, it ) }
     //   return this.collection().utilities.getValue( square )
    }

    override fun visit( square: TaxSquare ): SquareComponent<*> {
        return getActionSquareAndStoreIfNeeded( square )
    }

    private fun getActionSquareAndStoreIfNeeded( actionSquare: ActionSquare ): SquareComponent<*> {
      //  if ( false == this.collection().actionSquares.contains( actionSquare ) )
      return      ActionSquareComponent (
                square = actionSquare,
                squareController = this.squareController,
                squareDisplayStrategy = squareController.getSquareDisplayStrategy( actionSquare )
            )
     //           .let { this.collection().actionSquares.put( actionSquare, it ) }
     //   return this.collection().actionSquares.getValue( actionSquare )
    }

  //  private fun collection(): SquareComponentCollection {
  //      return this.componentCollections.getValue( clientState.dealPhase )
  // }

}