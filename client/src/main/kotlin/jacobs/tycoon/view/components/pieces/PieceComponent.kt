package jacobs.tycoon.view.components.pieces

import jacobs.mithril.DragEventHandler
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component
import org.js.mithril.VNode

class PieceComponent (
    private val playingPiece: PlayingPiece,
    private val pieceDisplayStrategy: PieceDisplayStrategy
) : Component {

    override fun view(): VNode {
        return m( Tag.div ) {
            attributes {
                draggable = true
            }
            eventHandlers {
                ondragstart = getDragHandler()
            }
            child( getDisplay() )
        }
    }

    private fun getDragHandler(): DragEventHandler {
        return { event ->
            event.dataTransfer!!.setData( "text/plain", playingPiece.name )
            event.dataTransfer!!.dropEffect = "move"
        }
    }

    private fun getDisplay(): VNode {
        return m( this.pieceDisplayStrategy.getPieceDisplayComponent( this.playingPiece ) )
    }

}