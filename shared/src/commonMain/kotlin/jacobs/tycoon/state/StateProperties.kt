package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers

interface StateProperties {
    val board: Board
    val pieceSet: PieceSet
    val players: GamePlayers
    var stage: GameStage
}