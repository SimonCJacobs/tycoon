package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.squares.Street
import kotlinx.serialization.Serializable

@Serializable
class Build (
    val streets: List < Street >,
    val newHouses: List < Int >
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.build( streets, newHouses, actorPosition )
            .also { setExecutionResult( it ) }
    }
}