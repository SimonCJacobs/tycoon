package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class SetPieces ( val pieceSet: PieceSet ) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState) {
        this.execute( gameState )
    }

    override suspend fun execute( gameState: GameState ) {
        gameState.game().board.pieceSet = pieceSet
        this.executedSuccessfully()
    }

}