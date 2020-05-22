package jacobs.tycoon.view.components.board.squares

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Square

abstract class SquareComponent < TSquare: Square > : Component {

    protected abstract val squareController: SquareController
    protected abstract val squareDisplayStrategy: SquareDisplayStrategy

    protected abstract val square: TSquare

    protected open var tableColumnCount = 1

    @Suppress( "unused" )
    final override fun view(): VNode {
        return m( Tag.td ) {
            attributes ( object {
                val `class` = squareDisplayStrategy.getClass()
                val colspan = tableColumnCount
            } )
            addSquareEventHandlers()
            children(
                getNameDisplay(),
                *getPiecesIfGameUnderway(),
                squareDisplayStrategy.extraNode()
            )
        }
    }

    @Suppress( "unused" )
    private fun getNameDisplay(): VNode {
        return m( Tag.h6 ) {
            addSquareEventHandlers()
            content( square.name )
        }
    }

    private fun getPiecesIfGameUnderway(): Array < VNode > {
        if ( this.squareController.isGameUnderway() )
            return getPieceComponentNodes().toTypedArray()
        else
            return emptyArray()
    }

    private fun getPieceComponentNodes(): List < VNode > {
        return this.squareController.getPiecesOnSquare( this.square )
            .map { m( squareController.getPieceComponent( it ) ) }
    }

    private fun HyperScriptBuilder.Details.addSquareEventHandlers() {
        eventHandlers {
            onclick = { squareDisplayStrategy.handleClick() }
            if ( squareController.isDropTarget( square ) ) {
                ondrop = { event -> squareController.notifyOfDrop( event ) }
                ondragenter = { it.preventDefault() }
                ondragover = { it.preventDefault() } // allow a drop IF also dragEnter allows
            }
        }
    }

}