package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActualGameController(kodein: Kodein ) : GameController {

    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameState by kodein.instance < GameState > ()

    override suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean {
        return this.gameState.game.addPlayer( playerName, playingPiece )
    }

    override suspend fun completeSignUp():Boolean {
        return this.gameState.game.completeSignUp()
    }

    override suspend fun newGame(): Boolean {
        this.gameState.game = this.gameFactory.newGame()
        return true
    }

    override suspend fun setBoard( board: Board ): Boolean {
        this.gameState.board = board
        return true
    }

    override suspend fun setPieces( pieces: PieceSet ): Boolean {
        this.gameState.game.pieceSet = pieces
        return true
    }

}