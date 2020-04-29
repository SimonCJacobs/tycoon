package jacobs.tycoon.view.components.pages

import jacobs.mithril.Mithril
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.pieces.PlayingPieceList
import kotlinx.coroutines.Deferred

class EntryPageState (
    availablePiecesDeferred: Deferred < PlayingPieceList >
) {

    init {
        availablePiecesDeferred.invokeOnCompletion {
            updateForAvailablePieces( availablePiecesDeferred.getCompleted() )
        }
    }

    private fun updateForAvailablePieces( availablePieces: PlayingPieceList ) {
        this.availablePieces = availablePieces
        this.selectedPiece = availablePieces.random()
        this.isReady = true
        Mithril().redraw()
    }

    lateinit var availablePieces: List < PlayingPiece >
    var isReady = false
    var playerName: String = ""
    lateinit var selectedPiece: PlayingPiece

}