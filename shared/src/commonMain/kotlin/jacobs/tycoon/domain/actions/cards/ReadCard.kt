package jacobs.tycoon.domain.actions.cards

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.results.ReadCardResult
import kotlinx.serialization.Serializable

@Serializable
class ReadCard : GameAction() {

    var result: ReadCardResult = ReadCardResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = gameController.readCard( actorPosition )
        this.executedSuccessfully()
    }

}