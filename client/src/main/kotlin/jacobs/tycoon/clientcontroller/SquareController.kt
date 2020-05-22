package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.board.squares.DestinationSquareDisplay
import jacobs.tycoon.view.components.board.squares.NormalPlayDisplay
import jacobs.tycoon.view.components.board.squares.SquareDisplayStrategy
import jacobs.tycoon.view.components.pieces.PieceComponentFactory
import kotlinx.coroutines.launch
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.DragEvent

class SquareController( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val dealController by kodein.instance < DealController > ()
    private val gameState: GameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val pieceComponentFactory by kodein.instance < PieceComponentFactory > ()

    fun getPieceComponent( playingPiece: PlayingPiece ): Component {
        return this.pieceComponentFactory.getForPiece( playingPiece )
    }

    fun getPiecesOnSquare( square: Square ): Set < PlayingPiece > {
        return this.gameState.game().board.getPiecesOnSquare( square )
    }

    fun getSquareDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            this.isDropTarget( square ) && this.isCorrectPieceBeingMovedByCorrectPlayer()
                -> DestinationSquareDisplay
            clientState.isComposingDeal -> dealController.getDealingSquareDisplayStrategy( square )
            else -> NormalPlayDisplay
        }
    }

    fun isDropTarget( square: Square ): Boolean {
        return this.gameState.game().isThisTheSquareToMoveTo( square )
    }

    /**
     * Handler is only registered on the destination square so no need to check if that is correct
     */
    fun notifyOfDrop( event: DragEvent ) {
        if ( this.isCorrectPieceBeingMovedByCorrectPlayer() ) {
            this.actOnSuccessfulPieceDrop()
        }
        event.preventDefault()
    }

    private fun actOnSuccessfulPieceDrop() {
        launch { outgoingRequestController.pieceMoved() }
    }

    private fun isCorrectPieceBeingMovedByCorrectPlayer(): Boolean {
        return this.isCorrectPieceBeingMoved() &&
            this.isTurnOfOwnPlayer()
    }

    private fun isCorrectPieceBeingMoved(): Boolean {
        return this.clientState.pieceBeingDragged
            ?.let { this.isThisTheIntendedMovingPiece( it ) }
            ?: false
    }

    private fun isThisTheIntendedMovingPiece( piece: PlayingPiece ): Boolean {
        return this.gameState.game().isThisThePieceToMove( piece )
    }

}
