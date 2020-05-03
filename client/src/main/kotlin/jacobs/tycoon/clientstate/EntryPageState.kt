package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.pieces.PlayingPiece

class EntryPageState {
    var showNoGameEntry = false
    var pieceOptionList: List < PlayingPiece > = listOf()
    var playerName: String = ""
    var selectedPiece: PlayingPiece? = null
}