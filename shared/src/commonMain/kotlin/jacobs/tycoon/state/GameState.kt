package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameState( kodein: Kodein ) {

    val board by kodein.instance < Board > ()
    val pieceSet by kodein.instance < PieceSet > ()
    val players by kodein.instance < GamePlayers > ()
    var stage: GameStage = GameStage.PLAYER_SIGN_UP

}