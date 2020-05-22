package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.property.MortgageOnTransferDecision
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player

class DealingWithMortgageInterestOnTransfer (
    override val playerWithTurn: Player,
    private val propertyOwner: Player,
    private val property: Property,
    private val phasePhactory: PhasePhactory
) : TurnBasedPhase {

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun doesOwnProperty( player: Player ): Boolean {
        return player.owns( this.property )
    }

    fun dealWithMortgage( decision: MortgageOnTransferDecision, game: Game ) {
        when( decision ) {
            MortgageOnTransferDecision.PAY_INTEREST_ONLY -> this.payInterestOnly( game.bank.interestRate )
            MortgageOnTransferDecision.PAY_OFF_IN_FULL -> this.payOffEntireMortgage( game )
        }
    }

    private fun payInterestOnly( interestRate: Float ) {
        this.phasePhactory.paymentDueToBank(
            playerWithTurn,
            propertyOwner,
            "mortgage interest",
            property.mortgageInterestAmount( interestRate )
        )
            .attemptPayment( propertyOwner )
    }

    private fun payOffEntireMortgage( game: Game ) {
        game.payOffMortgage( property, propertyOwner.position )
    }

}