package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Position
import jacobs.websockets.SocketId
import kotlinx.coroutines.Deferred
import kotlin.js.JsName

/**
 * Must include JavaScript names for each function so reflection employed in client-side code can find the methods
 * notwithstanding the name mangling done by the compiler
 *
 * Suspend functions are permitted because any use of the suspension facilities in JavaScript code will fail as
 * a result of the interaction with the hacky JS reflection employed upstream in StateSynchroniser (3.5.20)
 */
interface GameController {
    @JsName( "addPlayerAsync" ) fun addPlayerAsync(
        playerName: String, playingPiece: PlayingPiece, position: Position ): Deferred < Boolean >
    @JsName( "completeSignUpAsync" ) fun completeSignUpAsync(): Deferred < Boolean >
    @JsName( "newGameAsync" ) fun newGameAsync(): Deferred < Boolean >
    @JsName( "setBoardAsync" ) fun setBoardAsync( board: Board ): Deferred < Boolean >
    @JsName( "setPiecesAsync" ) fun setPiecesAsync( pieces: PieceSet ): Deferred < Boolean >
}