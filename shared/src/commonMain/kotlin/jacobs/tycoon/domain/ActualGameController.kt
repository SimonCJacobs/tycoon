package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Position
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActualGameController( kodein: Kodein ) : GameController {

    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameState by kodein.instance < GameState > ()

    override fun addPlayerAsync(playerName: String, playingPiece: PlayingPiece, position: Position ): Deferred < Boolean > {
        return this.coroutineScope.async {
            gameState.game().addPlayer( playerName, playingPiece, position )
        }
    }

    override fun completeSignUpAsync(): Deferred < Boolean > {
        return CompletableDeferred( this.gameState.game().completeSignUp() )
    }

    override fun newGameAsync(): Deferred < Boolean > {
        this.gameState.setGame( this.gameFactory.newGame() )
        return CompletableDeferred( true )
    }

    override fun setBoardAsync( board: Board ): Deferred < Boolean > {
        this.gameState.board = board
        return CompletableDeferred( true )
    }

    override fun setPiecesAsync( pieces: PieceSet ): Deferred < Boolean > {
        this.gameState.game().pieceSet = pieces
        return CompletableDeferred( true )
    }

}