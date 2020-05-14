package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.results.RentChargeOutcome
import jacobs.tycoon.domain.actions.results.RentChargeResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.PropertyVisitor
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules

class PotentialRentCharge (
    val playerOccupyingProperty: Player,
    playerWithTurnStarting: Player,
    val occupiedProperty: Property,
    jailRules: JailRules
) : RollingForMove( playerWithTurnStarting, jailRules ) {

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun canPlayerChargeRent( player: Player ): Boolean {
        return player.owns( this.occupiedProperty )
    }

    fun chargeRent( owner: Player ): RentChargeResult {
        val rentDue = this.getRentDue( owner )
        if ( rentDue > playerOccupyingProperty.cashHoldings )
            return this.getResult( rentDue, RentChargeOutcome.BANKRUPTCY_PROCEEDINGS )
        else
            return this.chargeRentAndGetResult( rentDue, owner )
    }

    private fun getRentDue( owner: Player ): CurrencyAmount {
        return this.occupiedProperty.accept( RentVisitor( owner, playerWithTurn.lastDiceRoll() ) )
    }

    private fun chargeRentAndGetResult( rentDue: CurrencyAmount, owner: Player ): RentChargeResult {
        completeRentTransaction( rentDue, owner )
        return this.getResult( rentDue, RentChargeOutcome.RENT_PAID )
    }

    private fun completeRentTransaction( rentDue: CurrencyAmount, owner: Player ) {
        playerOccupyingProperty.debitFunds( rentDue )
        owner.creditFunds( rentDue )
    }

    private fun getResult( rentDue: CurrencyAmount, outcome: RentChargeOutcome ): RentChargeResult {
        return RentChargeResult(
            rentDue = rentDue, outcome = outcome
        )
    }

    class RentVisitor( private val owner: Player, private val lastRoll: DiceRoll ) : PropertyVisitor < CurrencyAmount > {
        override fun visit( square: Station ): CurrencyAmount {
            return square.rentDue( this.owner )
        }
        override fun visit( square: Street ): CurrencyAmount {
            return square.rentDue( this.owner )
        }
        override fun visit( square: Utility ): CurrencyAmount {
            return square.rentDue( this.owner, this.lastRoll )
        }
    }

}