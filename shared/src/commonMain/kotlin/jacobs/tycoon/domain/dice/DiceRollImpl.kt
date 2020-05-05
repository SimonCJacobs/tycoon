package jacobs.tycoon.domain.dice

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class DiceRollImpl( private val numberOfSides: Int ) : DiceRoll {

    override val first: Int = this.rollOneDie()
    override val second: Int = this.rollOneDie()
    override val result: Int
        get() = first + second

    override fun isDouble(): Boolean {
        return this.first == this.second
    }

    private fun rollOneDie(): Int {
        return Random.nextInt( 1, numberOfSides + 1 )
    }

}