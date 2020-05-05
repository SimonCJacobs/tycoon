package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
class AddPlayer (
    val playerName: String,
    val playingPiece: PlayingPiece
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameState: GameState ) {
        this.execute( gameState )
    }

    override suspend fun execute( gameState: GameState ) {
        val result = gameState.game().addPlayer( playerName, playingPiece, actorPosition )
            .also { this.setExecutionResult( it ) }
        this.setExecutionResult( result )
    }

}