package jacobs.tycoon.domain.dice

import kotlinx.serialization.Serializable

@Serializable
abstract class DiceRoll : Comparable < DiceRoll > {

    abstract val first: Int
    abstract val second: Int
    abstract val result: Int

    abstract fun isDouble(): Boolean

    companion object {
        val NULL: DiceRoll = NullDiceRoll()
    }

    override fun compareTo( other: DiceRoll ): Int {
        return this.result - other.result
    }

}