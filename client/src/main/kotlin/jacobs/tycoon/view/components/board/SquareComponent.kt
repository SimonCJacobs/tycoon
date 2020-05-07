package jacobs.tycoon.view.components.board

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.jsutilities.jsObject
import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory

abstract class SquareComponent < TSquare: Square > : Component {

    protected abstract val squareController: SquareController

    protected abstract val name : String
    protected abstract val square: TSquare

    final override fun view(): VNode {
        return m( Tag.td ) {
            this.
            attributes {
                style = jsObject {
                    border = "1.2px solid black"
                    textAlign = "center"
                }
            }
            addAnyDropTargetHandlers()
            children(
                getNameDisplay(),
                *getPiecesIfGameUnderway()
            )
        }
    }

    private fun getNameDisplay(): VNode {
        return m( Tag.h6 ) { content( name ) }
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

    private fun HyperScriptBuilder.Details.addAnyDropTargetHandlers() {
        if ( false == squareController.isDropTarget( square ) ) return
        eventHandlers {
            ondrop = { squareController.actOnSuccessfulPieceDrop() }
            ondragenter = { event -> event.preventDefault() }
            ondragover = { event -> event.preventDefault() }
        }
    }

}