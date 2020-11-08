package jacobs.tycoon.domain.actions.cards

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction

class PayFineOrTakeCard(
    val decision: PayFineOrTakeCardDecision
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        return this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.payFineOrTakeCard( decision, actorPosition )
            .also { this.setExecutionResult( it ) }
    }
}