package jacobs.tycoon.services

import jacobs.tycoon.domain.dice.DiceRoll

fun DiceRoll.inWordAndNumber(): String {
    return ( if ( this.result.startsWithAVowelSound() ) "an " else "a " ) + this.result
}

private fun Int.startsWithAVowelSound(): Boolean {
    return when( this ) {
        1, 2, 3, 4, 5, 6, 7, 9, 10, 12 -> false
        8, 11 -> true
        else -> throw Error( "No dice rolls over 12 should occur" )
    }
}