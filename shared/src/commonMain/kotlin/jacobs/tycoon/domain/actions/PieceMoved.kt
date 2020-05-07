package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class PieceMoved : GameAction() {

    var result = MoveResult.NULL

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState ) {
        this.execute( gameState )
    }

    override suspend fun execute( gameState: GameState ) {
        this.result = gameState.game().movePiece()
        this.executedSuccessfully()
    }

}