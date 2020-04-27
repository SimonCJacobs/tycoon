package jacobs.tycoon.view.components.pages

import jacobs.tycoon.domain.pieces.PlayingPiece

class EntryPageState(
    var availablePieces: List < PlayingPiece >,
    var selectedPiece: PlayingPiece
) {
    var playerName: String = ""
    var testText = "Nothing"
}