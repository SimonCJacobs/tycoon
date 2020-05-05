package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class SetBoard ( val board: Board ) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState ) {
        this.execute( gameState )
    }

    override suspend fun execute( gameState: GameState ) {
        gameState.board = board
        this.executedSuccessfully()
    }

}