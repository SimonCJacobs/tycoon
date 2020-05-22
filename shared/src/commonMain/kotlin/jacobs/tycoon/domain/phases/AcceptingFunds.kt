package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player

class AcceptingFunds(
    override val playerWithTurn: Player,
    private val amountDue: CurrencyAmount
) : TurnBasedPhase {

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun acceptFunds() {
        playerWithTurn.creditFunds( amountDue )
    }

}