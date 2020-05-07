package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class SetBoard ( val board: Board ) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.game().board = board
        this.executedSuccessfully()
    }

}