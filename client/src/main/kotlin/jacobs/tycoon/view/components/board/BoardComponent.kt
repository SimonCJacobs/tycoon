package jacobs.tycoon.view.components.board

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.tycoon.domain.board.Board
import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.clientcontroller.BoardController
import jacobs.tycoon.view.components.board.centre.CentreCellReposifactory
import jacobs.tycoon.view.components.board.squares.JailComponent
import jacobs.tycoon.view.components.board.squares.SquareComponent
import jacobs.tycoon.view.components.board.squares.SquareComponentRepository
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class BoardComponent( kodein: Kodein ) : Component {

    private val boardController by kodein.instance < BoardController > ()
    private val centreCellReposifactory by kodein.instance < CentreCellReposifactory > ()
    private val squareComponentRepository by kodein.instance < SquareComponentRepository > ()

    private lateinit var jailComponent: JailComponent
    private lateinit var squares: List < SquareComponent < * > >

    private fun board(): Board {
        return boardController.board()
    }

    /**
     * All boards must have a square count of the form 4k + 4, and a board in such a form will have
     * a side of length k + 2
     */
    private val squaresToASide by lazy { ( board().squareList.size / 4 ) + 1 }
    private val squaresToASideExcludingCorners by lazy { squaresToASide - 2 }

    override fun view(): VNode {
        this.refreshSubComponents()
        return m( Tag.table ) {
            children( getTableChildren() )
        }
    }

    private fun refreshSubComponents() {
        this.jailComponent = squareComponentRepository.getForJailSquare(
            board().jailSquare, squaresToASideExcludingCorners
        )
        this.squares = board().squareList.map { squareComponentRepository.getFromSquare( it ) }
    }

    private fun getTableChildren(): List < VNode > {
        val leftSquares = this.getInnerLeftSideSquares()
        val rightSquares = this.getInnerRightSideSquares()
        val listOfLists =
            listOf(
                listOf(
                    this.getTopRowAsSquares().wrapInTableRow(),
                    this.getSecondRowGivenSecondSideSquares( leftSquares.first[ 0 ], rightSquares.first[ 0 ] )
                ),
                this.pairUpFromIndex( leftSquares.first, rightSquares.first, 1 )
                    .map { putPairInRow( it ) },
                this.getOneAboveBottomRowSquares( leftSquares.second, rightSquares.second ),
                listOf( this.getBottomRowAsSquares().wrapInTableRow() )
            )
        return listOfLists.flatten()
    }

    private fun getOneAboveBottomRowSquares(leftSideComponent: SquareComponent<*>,
                                            rightSideComponent: SquareComponent<*>): List < VNode > {
        val componentList = listOf(
            leftSideComponent, jailComponent, rightSideComponent
        )
        return listOf( componentList.wrapInTableRow() )
    }

   // private fun getEmptySquares( numberOfSquares: Int ): Array < VNode > {
   //     return ( 1 .. numberOfSquares ).map { m( Tag.td ) }.toTypedArray()
   // }

    private fun getTopRowAsSquares(): List <SquareComponent<*>> {
        return ( 1 .. squaresToASide )
            .map { it - 1 } // because list starts at 0
            .map { it + squaresToASide + squaresToASideExcludingCorners }
            .map { this.squares[ it ] }
    }

    private fun getSecondRowGivenSecondSideSquares(leftSquare: SquareComponent<*>,
                                                   rightSquare: SquareComponent<*>): VNode {
        return listOf(
            leftSquare,
            this.centreCellReposifactory.getCentreCell( squaresToASideExcludingCorners ),
            rightSquare
        )
            .wrapInTableRow()
    }

    private fun getInnerLeftSideSquares(): Pair < List <SquareComponent<*>>, SquareComponent<*>> {
        return Pair(
            this.getNaturalOrderedInnerSideLessOneStartingAtIndex( this.squaresToASide + 1 ).reversed(),
            this.squares.get( squaresToASide )
        )
    }

    private fun getInnerRightSideSquares(): Pair < List <SquareComponent<*>>, SquareComponent<*>> {
        return Pair(
            this.getNaturalOrderedInnerSideLessOneStartingAtIndex( 2 * squaresToASide + squaresToASideExcludingCorners ),
            this.squares.last()
        )
    }

    private fun getBottomRowAsSquares(): List <SquareComponent<*>> {
        return this.squares.take( this.squaresToASide ).reversed()
    }

    private fun getNaturalOrderedInnerSideLessOneStartingAtIndex( startIndex: Int ): List <SquareComponent<*>> {
        return ( startIndex until startIndex + squaresToASideExcludingCorners - 1 )
            .map { this.squares[ it ] }
    }

    private fun putPairInRow( pair: Pair <SquareComponent<*>, SquareComponent<*>> ): VNode {
        return m( Tag.tr ) {
            children( m( pair.first ), m( pair.second ) )
        }
    }

    private fun < T > pairUpFromIndex( listOne: List < T >, listTwo: List < T >, startIndex: Int ): List < Pair < T, T > > {
        return ( startIndex until listOne.size )
            .map { listOne[ it ] to listTwo[ it ] }
    }

    private fun List < Component >.wrapInTableRow() : VNode {
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