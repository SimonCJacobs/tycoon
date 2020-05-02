package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.Player
import kotlin.js.JsName

/**
 * Must include JavaScript names for each function so reflection employed in client-side code can find the methods
 * notwithstanding the name mangling done by the compiler
 */
interface GameStateUpdater {
    @JsName( "addPlayer" ) suspend fun addPlayer( player: Player )
    @JsName( "setBoard" ) suspend fun setBoard( board: Board )
    @JsName( "setPieces" ) suspend fun setPieces( pieces: PieceSet )
    @JsName( "updateStage" ) suspend fun updateStage( newGameStage: GameStage )
}