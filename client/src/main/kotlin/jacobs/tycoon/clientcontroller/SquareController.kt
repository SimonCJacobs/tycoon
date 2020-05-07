package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory
import kotlinx.coroutines.launch
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class SquareController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val gameState: GameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val pieceComponentFactory by kodein.instance < PieceComponentFactory > ()

    fun actOnSuccessfulPieceDrop() {
        launch { outgoingRequestController.pieceMoved() }
    }

    fun getPieceComponent( playingPiece: PlayingPiece ): Component {
        return this.pieceComponentFactory.getForPiece( playingPiece )
    }

    fun isDropTarget( square: Square ): Boolean {
        return this.gameState.game().phase
            .let { it is MovingAPiece && it.isSquareDropTarget( square ) }
    }

    fun isGameUnderway(): Boolean {
        return this.gameState.game().isGameUnderway()
    }

    fun getPiecesOnSquare( square: Square ): Set < PlayingPiece > {
        return this.gameState.game().board.getPiecesOnSquare( square )
    }

}