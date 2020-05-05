package jacobs.tycoon.domain.actions

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

    override suspend fun duplicate( gameState: GameState ) {
        this.execute( gameState )
    }

    override suspend fun execute( gameState: GameState ) {
        this.executedSuccessfully()
    }
}