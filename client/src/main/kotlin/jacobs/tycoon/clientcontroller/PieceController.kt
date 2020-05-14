package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PieceController ( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val pieceDisplayStrategy by kodein.instance <PieceDisplayStrategy> ()

    fun getPieceDisplayComponent( playingPiece: PlayingPiece): Component {
        return this.pieceDisplayStrategy.getPieceDisplayComponent( playingPiece )
    }

    fun removePieceInDrag() {
        this.clientState.pieceBeingDragged = null
    }

    fun setPieceInDrag( playingPiece: PlayingPiece ) {
        this.clientState.pieceBeingDragged = playingPiece
    }

}