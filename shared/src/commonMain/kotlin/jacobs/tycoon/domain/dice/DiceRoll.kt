package jacobs.tycoon.domain.dice

import kotlinx.serialization.Serializable

@Serializable
abstract class DiceRoll : Comparable < DiceRoll > {

    companion object {
        val NULL: DiceRoll = NullDiceRoll()
    }

    abstract val first: Int
    abstract val second: Int
    val result: Int
        get() = first + second

    fun isDouble(): Boolean {
        return this.first == this.second
    }

    override fun compareTo( other: DiceRoll ): Int {
        return this.result - other.result
    }

}