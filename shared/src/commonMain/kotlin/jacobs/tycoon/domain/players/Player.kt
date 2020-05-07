package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val piece: PlayingPiece,
    val position: SeatingPosition,
    var cashHoldings: CurrencyAmount,
    private val toJailDoubleCount: Int
) : Comparable < Player > {

    companion object {
        val NULL = Player(
            name = "",
            piece = PlayingPiece.NULL,
            position = SeatingPosition.UNINITIALISED,
            cashHoldings = CurrencyAmount.NULL,
            toJailDoubleCount = 0
        )
    }

    private val rollRecord: MutableList < DiceRoll > = mutableListOf()

    fun addDiceRoll( diceRoll: DiceRoll ) {
        this.rollRecord.add( diceRoll )
    }

    fun creditFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings + amount
    }

    fun debitFunds( amount: CurrencyAmount ) {
        //TODO: Like an end of turn check the situtation tto see who's bankrupt
        this.cashHoldings = this.cashHoldings - amount
    }

    fun hasRolledMaximumDoublesInARow(): Boolean {
        return this.rollRecord.size >= toJailDoubleCount &&
            this.rollRecord.subList( this.rollRecord.size - toJailDoubleCount, this.rollRecord.size )
                .all { it.isDouble() }
    }

    fun isPosition( otherPosition: SeatingPosition ): Boolean {
        return this.position == otherPosition
    }

    fun moveToSquareAndMaybePassGo( newSquare: Square ): Boolean {
        val didPassGo = newSquare.indexOnBoard < this.piece.square.indexOnBoard
        this.piece.moveToSquare( newSquare )
        return didPassGo
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }

}