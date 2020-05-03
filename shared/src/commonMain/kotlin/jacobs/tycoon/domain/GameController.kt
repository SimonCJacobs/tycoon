package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlin.js.JsName

/**
 * Must include JavaScript names for each function so reflection employed in client-side code can find the methods
 * notwithstanding the name mangling done by the compiler
 */
interface GameController {
    @JsName( "addPlayer" ) suspend fun addPlayer( playerName: String, playingPiece: PlayingPiece ): Boolean
    @JsName( "completeSignUp" ) suspend fun completeSignUp(): Boolean
    @JsName( "newGame" ) suspend fun newGame(): Boolean
    @JsName( "setBoard" ) suspend fun setBoard( board: Board ): Boolean
    @JsName( "setPieces" ) suspend fun setPieces( pieces: PieceSet ): Boolean
}