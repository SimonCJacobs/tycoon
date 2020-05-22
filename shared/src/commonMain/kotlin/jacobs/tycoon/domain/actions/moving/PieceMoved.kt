package jacobs.tycoon.domain.actions.moving

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.results.MoveResult
import kotlinx.serialization.Serializable

@Serializable
class PieceMoved : GameAction() {

    var result = MoveResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = gameController.completePieceMove( this.actorPosition )
        this.executedSuccessfully()
    }

}