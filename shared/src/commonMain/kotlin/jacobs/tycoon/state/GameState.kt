package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameState( kodein: Kodein ) {

    val board: Board by kodein.instance()
    val pieceSet: PieceSet by kodein.instance()
    val players: GamePlayers by kodein.instance()
    var stage: GameStage = GameStage.PLAYER_SIGN_UP

}