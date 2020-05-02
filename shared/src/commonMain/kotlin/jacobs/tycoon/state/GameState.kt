package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers

class GameState {

    lateinit var board: Board
    lateinit var pieceSet: PieceSet
    val players: GamePlayers = GamePlayers()
    lateinit var stage: GameStage

}
