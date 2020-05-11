package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceComponentFactory
import kotlinx.coroutines.launch
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.DragEvent

class SquareController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val clientState: ClientState by kodein.instance < ClientState > ()
    private val gameState: GameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val pieceComponentFactory by kodein.instance < PieceComponentFactory > ()

    fun actOnSuccessfulPieceDrop() {
        launch { outgoingRequestController.pieceMoved() }
    }

    fun getPieceComponent( playingPiece: PlayingPiece ): Component {
        return this.pieceComponentFactory.getForPiece( playingPiece )
    }

    fun isDropTarget( square: Square): Boolean {
        return this.gameState.game().isThisTheSquareToMoveTo( square )
    }

    fun getPiecesOnSquare( square: Square): Set < PlayingPiece > {
        return this.gameState.game().board.getPiecesOnSquare( square )
    }

    fun isThisValidDestinationAndActorForDrop(): Boolean {
        return this.isThisValidDestination() &&
            this.isTurnOfOwnPlayer()
    }

    private fun isThisValidDestination(): Boolean {
        return this.clientState.pieceInDrag
            .let {
                if ( it == null )
                    false
                else
                    this.isThisTheIntendedMovingPiece( it )
            }
    }

    private fun isThisTheIntendedMovingPiece( piece: PlayingPiece ): Boolean {
        return this.gameState.game().isThisThePieceToMove( piece )
    }

}