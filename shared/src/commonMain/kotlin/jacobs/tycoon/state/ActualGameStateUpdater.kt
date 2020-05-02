package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActualGameStateUpdater( kodein: Kodein ) : GameStateUpdater {

    private val gameState by kodein.instance < GameState > ()

    override suspend fun addPlayer( player: Player ) {
        this.gameState.players.addPlayer( player )
    }

    override suspend fun setBoard( board: Board ) {
        this.gameState.board = board
    }

    override suspend fun setPieces( pieces: PieceSet ) {
        this.gameState.pieceSet = pieces
    }

    override suspend fun updateStage(newGamePhase: GamePhase ) {
        this.gameState.phase = newGamePhase
    }
}