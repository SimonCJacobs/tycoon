package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

/**
 * This class doesn't execute anything; it's an action merely for recording purposes and a new game is
 * started independently
 */
@Serializable
class NewGame : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.executedSuccessfully()
    }
}