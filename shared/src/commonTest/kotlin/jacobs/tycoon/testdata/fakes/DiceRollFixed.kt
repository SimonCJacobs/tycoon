package jacobs.tycoon.testdata.fakes

import jacobs.tycoon.domain.dice.DiceRoll

class DiceRollFixed(
    override val first: Int,
    override val second: Int
) : DiceRoll() {

    constructor( dicePair: Pair < Int, Int > ) : this( dicePair.first, dicePair.second )

}