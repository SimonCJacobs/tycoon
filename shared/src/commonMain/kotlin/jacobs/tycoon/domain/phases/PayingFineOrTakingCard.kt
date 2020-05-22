package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCardDecision
import jacobs.tycoon.domain.phases.results.PayFineOrTakeChanceOutcome
import jacobs.tycoon.domain.players.Player

class PayingFineOrTakingCard(
    override val playerWithTurn: Player,
    private val paymentDue: PaymentDue,
    val cardReading: CardReading
) : TurnBasedPhase {

    lateinit var outcome: PayFineOrTakeChanceOutcome

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun carryOutDecision( decision: PayFineOrTakeCardDecision ) {
        when( decision ) {
            PayFineOrTakeCardDecision.PAY_FINE -> payFine()
            PayFineOrTakeCardDecision.TAKE_CARD -> takeChance()
        }
    }

    private fun payFine(): PayFineOrTakeChanceOutcome {
        this.paymentDue.attemptPayment( playerWithTurn )
        this.outcome = PayFineOrTakeChanceOutcome.PAY_FINE
        return this.outcome
    }

    private fun takeChance(): PayFineOrTakeChanceOutcome {
        this.outcome = PayFineOrTakeChanceOutcome.TAKE_CHANCE
        return this.outcome
    }

}