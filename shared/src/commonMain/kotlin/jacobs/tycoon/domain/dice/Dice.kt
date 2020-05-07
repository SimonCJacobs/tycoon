package jacobs.tycoon.domain.dice

class Dice {

    companion object {
        const val NUMBER_OF_SIDES = 6
    }

    var lastRoll: DiceRoll = DiceRoll.NULL
        private set

    fun haveBeenRolled(): Boolean {
        return this.lastRoll != DiceRoll.NULL
    }

    fun roll( diceRoll: DiceRoll? = null ): DiceRoll {
        this.lastRoll = diceRoll ?: this.rollForReal()
        return this.lastRoll
    }

    private fun rollForReal(): DiceRoll {
        return DiceRollImpl( NUMBER_OF_SIDES )
    }

}