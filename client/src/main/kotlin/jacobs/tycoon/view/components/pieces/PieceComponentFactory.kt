package jacobs.tycoon.view.components.pieces

import jacobs.tycoon.clientcontroller.PieceController
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PieceComponentFactory( kodein: Kodein ) {

    private val pieceController by kodein.instance < PieceController > ()

    fun getForPiece( piece: PlayingPiece ): Component {
        return PieceComponent( piece, this.pieceController )
    }

}