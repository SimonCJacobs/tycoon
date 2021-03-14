package jacobs.tycoon.domain.dice

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class DiceRollImpl
private constructor (
    override val first: Int,
    override val second: Int
) : DiceRoll() {

    companion object {
        fun random( numberOfSides: Int ): DiceRollImpl {
            return DiceRollImpl( rollOneDie( numberOfSides ), rollOneDie( numberOfSides ) )
        }

        private fun rollOneDie( numberOfSides: Int ): Int {
            return Random.nextInt( 1, numberOfSides + 1 )
        }
    }

}