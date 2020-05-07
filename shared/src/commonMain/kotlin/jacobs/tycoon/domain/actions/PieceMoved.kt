package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class PieceMoved : GameAction() {

    var result = MoveResult.NULL

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = gameController.game().movePieceCompleted()
        this.executedSuccessfully()
    }

}