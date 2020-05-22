package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.squares.Property
import kotlinx.serialization.Serializable

@Serializable
class MortgageProperty (
    val properties: Collection < Property >
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        properties.all { eachProperty ->
            gameController.mortgageProperty( eachProperty, actorPosition )
        }
            .also { setExecutionResult( it ) }

    }
}