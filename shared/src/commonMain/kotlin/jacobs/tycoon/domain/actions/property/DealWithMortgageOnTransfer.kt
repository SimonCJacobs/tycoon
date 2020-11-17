package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.players.SeatingPosition

class DealWithMortgageOnTransfer(
    val decision: MortgageOnTransferDecision,
    override val playerPosition: SeatingPosition
) : PositionalGameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController) {
        gameController.payOffMortgageOnTransfer( decision, playerPosition )
            .also {
                setExecutionResult( it )
            }
    }
}