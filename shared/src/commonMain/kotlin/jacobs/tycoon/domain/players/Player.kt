package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val piece: PlayingPiece,
    val position: SeatingPosition
) : Comparable < Player > {

    companion object {
        const val DOUBLES_IN_A_ROW_LIMIT = 3
        val NULL = Player( "", PlayingPiece.NULL, SeatingPosition.UNINITIALISED )
    }

    private val rollRecord: MutableList < DiceRoll > = mutableListOf()

    fun addDiceRoll( diceRoll: DiceRoll ) {
        this.rollRecord.add( diceRoll )
    }

    fun hasRolledMaximumDoublesInARow(): Boolean {
        return this.rollRecord.size >= DOUBLES_IN_A_ROW_LIMIT &&
            this.rollRecord.subList( this.rollRecord.size - DOUBLES_IN_A_ROW_LIMIT, this.rollRecord.size )
                .all { it.isDouble() }
    }

    fun isPosition( otherPosition: SeatingPosition ): Boolean {
        return this.position == otherPosition
    }

    fun moveToSquare( newSquare: Square ) {
        this.piece.moveToSquare( newSquare )
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }

}