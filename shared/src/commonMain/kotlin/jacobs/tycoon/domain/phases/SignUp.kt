package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game

object SignUp : GamePhase {

    fun completeSignUp( game: Game ) {
        game.board.pieceSet.freezePiecesInUse( game.players )
    }

}