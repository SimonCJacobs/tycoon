package jacobs.tycoon.domain.phases.rent

import jacobs.tycoon.domain.phases.results.RentChargeResult
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.PropertyVisitor
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.TurnBasedPhaseVisitor
import jacobs.tycoon.domain.players.Player

class PotentialRentCharge (
    private val playerOccupyingProperty: Player,
    val occupiedProperty: Property,
    private var turnsBeenChargeableVariable: Int = 0
) {
    var rentChargeResult: RentChargeResult = RentChargeResult.NULL
    val turnsBeenChargeable: Int
        get() = turnsBeenChargeableVariable

    fun canPlayerChargeRentOnProperty( player: Player ): Boolean {
        return player.owns( this.occupiedProperty ) && false == this.rentChargeResult.wasRentCharged
    }

    fun canPlayerChargeRentOnGivenProperty( player: Player, property: Property ): Boolean {
        return this.occupiedProperty == property && this.canPlayerChargeRentOnProperty( player )
    }

    fun incrementTurns() {
        turnsBeenChargeableVariable++
    }

    fun recordRentCharge( owner: Player ): RentChargeResult {
        if ( rentChargeResult.wasRentCharged == true )
            throw Error( "Rent cannot be paid twice" )
        this.rentChargeResult = RentChargeResult(
            propertyOwner = owner,
            rentDue = getRentDue( owner ),
            occupyingPlayer = playerOccupyingProperty,
            occupiedProperty = occupiedProperty,
            wasRentCharged = true
        )
        return this.rentChargeResult
    }

    private fun getRentDue( owner: Player ): CurrencyAmount {
        return this.occupiedProperty.accept(RentVisitor(owner, playerOccupyingProperty.lastDiceRoll()))
    }

    class RentVisitor( private val owner: Player, private val lastRoll: DiceRoll ): PropertyVisitor < CurrencyAmount > {
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