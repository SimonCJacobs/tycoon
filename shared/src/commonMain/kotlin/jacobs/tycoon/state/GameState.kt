package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameState( kodein: Kodein ) {

    val minimumNumberOfPlayers by kodein.instance < Int > ( tag = "minimumPlayers" )

    lateinit var board: Board
    lateinit var pieceSet: PieceSet
    val players: GamePlayers = GamePlayers()
    lateinit var phase: GamePhase

}
