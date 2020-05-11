package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import kotlinx.serialization.Serializable

@Serializable
class RespondToPropertyOffer(
    val decidedToBuy: Boolean
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController) {
        gameController.respondToPropertyOffer( this.decidedToBuy, position = actorPosition )
        this.executedSuccessfully()
    }

}