package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.GameActionWithResponse
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.websockets.content.Messageable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class AddPlayer (
    val playerName: String,
    val playingPiece: PlayingPiece
) : GameActionWithResponse() {

    @Transient // Transient because this not intended to be broadcast to all players in game
    lateinit var assignedPosition: SeatingPosition

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        val maybePosition = gameController.addPlayer( playerName, playingPiece )
        assignedPosition = maybePosition ?: SeatingPosition.UNASSIGNABLE
        this.setExecutionResult( null != maybePosition )
    }

    override fun getResponse(): SeatingPosition {
        return assignedPosition
    }

}