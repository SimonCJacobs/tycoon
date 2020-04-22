package jacobs.tycoon.view.components.board

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.jsutilities.jsObject
import jacobs.tycoon.domain.board.Board
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
        val leftSquares = this.getInnerLeftSideSquares()
        val rightSquares = this.getInnerRightSideSquares()
        val listOfLists =
            listOf(
                listOf(
                    this.getTopRowAsSquares().wrapInTableRow(),
                    this.getSecondRowGivenSecondSideSquares( leftSquares[ 0 ], rightSquares[ 0 ] )
                ),
                this.pairUpFromIndex( leftSquares, rightSquares, 1 )
                    .map { putPairInRow( it ) },
                listOf( this.getBottomRowAsSquares().wrapInTableRow() )
            )
        return listOfLists.flatten()
    }

    private fun getTopRowAsSquares(): List < SquareComponent > {
        return ( 1 .. squaresToASide )
            .map { it - 1 } // because list starts at 0
            .map { it + squaresToASide + squaresToASideExcludingCorners }
            .map { this.squares[ it ] }
    }

    private fun getSecondRowGivenSecondSideSquares( leftSquare: SquareComponent, rightSquare: SquareComponent )
            : VNode {
        return listOf( m( leftSquare), this.getEmptyCentreCell(), m( rightSquare ) )
            .wrapInTableRow()
    }

    private fun getInnerLeftSideSquares(): List < SquareComponent > {
        return this.getNaturalOrderedInnerSideStartingAtIndex( this.squaresToASide )
            .reversed()
    }

    private fun getInnerRightSideSquares(): List < SquareComponent > {
        return this.getNaturalOrderedInnerSideStartingAtIndex(
            2 * squaresToASide + squaresToASideExcludingCorners
        )
    }

    private fun getBottomRowAsSquares(): List < SquareComponent > {
        return this.squares.take( this.squaresToASide ).reversed()
    }

    private fun getNaturalOrderedInnerSideStartingAtIndex( startIndex: Int ): List < SquareComponent > {
        return ( startIndex until startIndex + squaresToASideExcludingCorners )
            .map { this.squares[ it ] }
    }

    private fun getEmptyCentreCell(): VNode {
        return m( Tag.td ) {
            attributes {
                colspan = squaresToASideExcludingCorners
                rowspan = squaresToASideExcludingCorners
                style = jsObject {
                    fontStyle = "italic"
                    fontSize = "80px"
                    textAlign = "center"
                    transform = "rotate( -0.048turn )"
                }
            }
            content( "Monopolisation" )
        }
    }

    private fun putPairInRow( pair: Pair < SquareComponent, SquareComponent > ): VNode {
        return m( Tag.tr ) {
            children( m( pair.first ), m( pair.second ) )
        }
    }

    private fun < T > pairUpFromIndex( listOne: List < T >, listTwo: List < T >, startIndex: Int ): List < Pair < T, T > > {
        return ( startIndex until listOne.size )
            .map { listOne[ it ] to listTwo[ it ] }
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