package jacobs.tycoon.domain.dice

interface DiceRoll {

    val first: Int
    val second: Int
    val result: Int

    fun isDouble(): Boolean

    companion object {
        val UNROLLED: DiceRoll = NullDiceRoll()
    }

}