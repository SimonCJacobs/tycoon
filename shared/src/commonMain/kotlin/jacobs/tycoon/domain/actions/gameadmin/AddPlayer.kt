package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
class AddPlayer (
    val playerName: String,
    val playingPiece: PlayingPiece
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        val result = gameController.addPlayer( playerName, playingPiece, actorPosition )
            .also { this.setExecutionResult( it ) }
        this.setExecutionResult( result )
    }

}