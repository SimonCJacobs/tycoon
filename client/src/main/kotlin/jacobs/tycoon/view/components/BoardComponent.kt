package jacobs.tycoon.view.components

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.tycoon.domain.Board
import jacobs.mithril.m
import jacobs.mithril.Tag

class BoardComponent(
    board: Board,
        // TODO: Construction of the components can probably happen in DI container
    squareComponentFactory: SquareComponentFactory
) : Component {

    private val squares: List < SquareComponent >
        = board.squareList.map { squareComponentFactory.getFromSquare( it ) }
    /**
     * All boards must have a square count of the form 4k + 4, and a board in such a form will have
     * a side of length k + 2
     */
    private val squaresToASide = ( board.squareList.size / 4 ) + 1
    private val squaresToASideExcludingCorners = squaresToASide - 2

    override fun view(): VNode {
        return m( Tag.table ) {
            children( getTableChildren() )
        }
    }

    private fun getTableChildren(): List < VNode > {
        val extremeRows =
            this.getTopRowOfSquares().wrapInTableRow() to
            this.getBottomRowOfSquares().wrapInTableRow()
        val intermediateRows =
            this.getSideSquaresInBoardConfiguration()
                .map { m( it.first ) to m( it.second ) }
                .map { it.insertListBetween( getRowOfEmptyCellsForBoardCentre() ) }
                .map { it.wrapInTableRow() }
        return extremeRows.insertListBetween( intermediateRows )
    }

    private fun getTopRowOfSquares(): List < SquareComponent > {
        return ( 1 .. squaresToASide )
            .map { it - 1 } // because list starts at 0
            .map { it + squaresToASide + squaresToASideExcludingCorners }
            .map { this.squares[ it ] }
    }

    private fun getSideSquaresInBoardConfiguration(): List < Pair < SquareComponent, SquareComponent > > {
            // The indices of opposing pairs must add to the same number if you think about it
            // Let's calculate that constant using the first row
        val sumOfOpposingPairs = squaresToASide + ( this.squares.size - 1 )
        return ( 1 .. squaresToASideExcludingCorners )
            .map { it - 1 } // because list starts at 0
            .map { it + squaresToASide } // now we have the square indices we want on the left
            .map { it to sumOfOpposingPairs - it } // now use the opposing pair sum
            .map { this.squares[ it.first ] to this.squares[ it.second ] }
            .reversed()
    }

    private fun getBottomRowOfSquares(): List < SquareComponent > {
        return this.squares.take( this.squaresToASide ).reversed()
    }

    private fun getRowOfEmptyCellsForBoardCentre(): List < VNode > {
        return ( 1 .. this.squaresToASideExcludingCorners )
            .map { getNewEmptyCell() }
    }

    private fun getNewEmptyCell(): VNode {
        return m( Tag.td )
    }

    private fun < T > Pair < T, T >.insertListBetween( list: List < T > ): List < T > {
        return listOf( listOf( this.first ), list, listOf( this.second ) )
            .flatten()
    }

    private fun List < SquareComponent >.wrapInTableRow() : VNode {
        val componentList = this
        return m( Tag.tr ) {
            children( m.map( componentList ) )
        }
    }

    private fun List < VNode >.wrapInTableRow() : VNode {
        val nodeList = this
        return m( Tag.tr ) {
            children( nodeList )
        }
    }

}