package jacobs.tycoon.domain.board.squares

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
class Utility(
    override val indexOnBoard: Int,
    override val name: String,
    override val listPrice: CurrencyAmount,
    private val utilityMultipliers: UtilityMultipliers
) : Property() {

    override fun < T > accept( propertyVisitor: PropertyVisitor < T > ): T {
        return propertyVisitor.visit( this )
    }

    override fun < T > accept( squareVisitor: SquareVisitor < T > ): T {
        return squareVisitor.visit( this )
    }

    fun rentDue( owner: Player, diceRoll: DiceRoll ): CurrencyAmount {
        if ( false == owner.owns( this ) )
            throw NotTurnOfPlayerException()
        else
            return this.calculateRent( owner, diceRoll )
    }

    private fun calculateRent( owner: Player, diceRoll: DiceRoll ): CurrencyAmount {
        return when ( val utilityCount = owner.howManyUtilitiesOwned() ) {
            1 -> utilityMultipliers.withOneUtility * diceRoll.result
            2 -> utilityMultipliers.withTwoUtilities * diceRoll.result
            else -> throw Error( "Should not get here: player owns $utilityCount utilities" )
        }
    }

}