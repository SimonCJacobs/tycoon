package jacobs.tycoon.view.components.pieces

import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component

interface PieceDisplayStrategy {
    fun getPieceDisplayComponent( playingPiece: PlayingPiece ): Component
}