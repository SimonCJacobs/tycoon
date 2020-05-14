package jacobs.tycoon.view.components.pieces

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PieceController
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component
import org.js.mithril.VNode

class PieceComponent (
    private val playingPiece: PlayingPiece,
    private val pieceController: PieceController
) : Component {

    @Suppress( "unused" )
    override fun view(): VNode {
        return m( Tag.div ) {
            attributes ( object {
                val draggable = true // Pieces are always draggable. Why not? :)
                val style = object {
                    val cursor = "grab"
                    val display = "inline-block"
                    val padding = "4px"
                }
            } )
            eventHandlers {
                ondragstart = { pieceController.setPieceInDrag( playingPiece ) }
                ondragend = { pieceController.removePieceInDrag() }
            }
            child( getDisplay() )
        }
    }

    private fun getDisplay(): VNode {
        return m( this.pieceController.getPieceDisplayComponent( this.playingPiece ) )
    }

}