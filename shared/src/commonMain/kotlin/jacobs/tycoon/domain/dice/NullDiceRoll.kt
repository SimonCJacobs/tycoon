package jacobs.tycoon.domain.dice

import kotlinx.serialization.Serializable

@Serializable
class NullDiceRoll: DiceRoll() {

    override val first: Int
        get() = throwNotToBeUsedError()
    override val second: Int
        get() = throwNotToBeUsedError()
    override val result: Int
        get() = throwNotToBeUsedError()

    override fun isDouble() = throwNotToBeUsedError()

    private fun throwNotToBeUsedError(): Nothing = throw Error( "Unrolled DiceRoll should not be used" )

}