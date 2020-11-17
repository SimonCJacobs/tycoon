package jacobs.tycoon.domain.actions.cards

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
class PayFineOrTakeCard(
    val decision: PayFineOrTakeCardDecision,
    override val playerPosition: SeatingPosition
) : PositionalGameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        return this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.payFineOrTakeCard( decision, playerPosition )
            .also { this.setExecutionResult( it ) }
    }
}