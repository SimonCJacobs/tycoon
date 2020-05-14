package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import kotlinx.serialization.Serializable

@Serializable
class CompleteSignUp : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.completeSignUp()
        this.executedSuccessfully()
    }
}