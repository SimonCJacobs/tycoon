package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.players.Player

/**
 * Properties that compute rent based on ownership only, not dice roll
 */
abstract class OwnershipProperty : Property() {

    fun rentDue( owner: Player): CurrencyAmount {
        if ( false == owner.owns( this ) )
            throw NotTurnOfPlayerException()
        else
            return this.calculateRent( owner )
    }

    protected abstract fun calculateRent( owner: Player): CurrencyAmount

}