package jacobs.tycoon.domain.dice

class Dice {

    companion object {
        const val NUMBER_OF_SIDES = 6
    }

    var lastRoll: DiceRoll = DiceRoll.UNROLLED
        private set

    fun roll( knownDiceRoll: DiceRoll? = null ): DiceRoll {
        this.lastRoll = knownDiceRoll ?: this.rollForReal()
        return this.lastRoll
    }

    private fun rollForReal(): DiceRoll {
        return DiceRollImpl( NUMBER_OF_SIDES )
    }

}