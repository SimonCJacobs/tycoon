package jacobs.tycoon.view.components.pieces

import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PieceComponentFactory( kodein: Kodein ) {

    private val pieceDisplayStrategy by kodein.instance < PieceDisplayStrategy > ()

    fun getForPiece( piece: PlayingPiece ): Component {
        return PieceComponent( piece, this.pieceDisplayStrategy )
    }

}