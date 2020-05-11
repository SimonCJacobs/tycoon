package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.results.RentChargeResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.PropertyVisitor
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player

class PotentialRentCharge (
    val playerOccupyingProperty: Player,
    playerWithTurnStarting: Player,
    val occupiedProperty: Property
) : RollingForMove( playerWithTurnStarting ) {

    var requireBankruptcyProceedings = false
        private set

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun canPlayerChargeRent( player: Player ): Boolean {
        return player.owns( this.occupiedProperty )
    }

    fun chargeRent( owner: Player ): RentChargeResult {
        val rentDue = this.getRentDue( owner )
        if ( rentDue > playerOccupyingProperty.cashHoldings )
            this.requireBankruptcyProceedings = true
        else
            this.completeRentTransaction( rentDue, owner )
        return this.generateResult( rentDue )
    }

    private fun getRentDue( owner: Player ): CurrencyAmount {
        return this.occupiedProperty.accept( RentVisitor( owner, playerWithTurn.lastDiceRoll() ) )
    }

    private fun generateResult( rentDue: CurrencyAmount ): RentChargeResult {
        return RentChargeResult(
            rentDue = rentDue
        )
    }

    private fun completeRentTransaction( rentDue: CurrencyAmount, owner: Player ) {
        playerOccupyingProperty.debitFunds( rentDue )
        owner.creditFunds( rentDue )
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