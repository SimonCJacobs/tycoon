package jacobs.tycoon.view.components.board

import org.js.mithril.Component
import org.js.mithril.VNode
import jacobs.mithril.DragEventHandler
import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.clientcontroller.SquareController
import jacobs.tycoon.domain.board.squares.Square

abstract class SquareComponent < TSquare: Square > : Component {

    protected abstract val squareController: SquareController

    protected abstract val name : String
    protected abstract val square: TSquare

    private var dragIsOverhead = false
    protected open var tableColumnCount = 1

    @Suppress( "unused" )
    final override fun view(): VNode {
        return m( Tag.td ) {
            this.
            attributes ( object {
                val colspan = tableColumnCount
                val style = object {
                    val backgroundColor = if ( dragIsOverhead ) "orange" else "white"
                    val border = "1.2px solid black"
                    var cursor = if ( dragIsOverhead ) "move" else "auto"
                    val textAlign = "center"
                }
            } )
            addDropTargetHandlersIfDestination()
            children(
                getNameDisplay(),
                *getPiecesIfGameUnderway()
            )
        }
    }

    @Suppress( "unused" )
    private fun getNameDisplay(): VNode {
        return m( Tag.h6 ) {
            attributes ( object {
                val style = object {
                        // Stops drag getting forgotten when over this element
                    val pointerEvents = if ( dragIsOverhead ) "none" else "auto"
                }
            } )
            addDropTargetHandlersIfDestination()
            content( name )
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

    private fun HyperScriptBuilder.Details.addDropTargetHandlersIfDestination() {
        if ( false == squareController.isDropTarget( square ) ) return
        eventHandlers {
            ondrop = onDrop()
            ondragenter = onDragEnter()
            ondragleave = onDragLeave()
            ondragover = { it.preventDefault() } // allow a drop IF also dragEnter allows
        }
    }

    private fun onDragEnter(): DragEventHandler {
        return { event ->
            if ( squareController.isThisValidDestinationAndActorForDrop() ) {
                event.preventDefault() // Browser knows from this valid target
                dragIsOverhead = true
            }
        }
    }

    private fun onDragLeave(): DragEventHandler {
        return {
            if ( squareController.isThisValidDestinationAndActorForDrop() )
                dragIsOverhead = false
        }
    }

    private fun onDrop(): DragEventHandler {
        return { event ->
            if ( squareController.isThisValidDestinationAndActorForDrop() ) {
                dragIsOverhead = false
                squareController.actOnSuccessfulPieceDrop()
            }
            event.preventDefault()
        }
    }

}