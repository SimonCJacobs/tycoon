package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class Board {

    abstract val location: String
    protected abstract val currency: Currency
    @Transient lateinit var pieceSet: PieceSet
    abstract val squareList: List <Square>

    companion object {
        private val JAIL_SQUARE = JailSquare()
    }

    // Protected API

    protected fun Int.toCurrency(): CurrencyAmount {
        return currency.ofAmount( this )
    }

    // Public API

    fun getJailSquare(): Square {
        return JAIL_SQUARE
    }

    fun getPieceCount(): Int {
        return this.pieceSet.count()
    }

    fun getPiecesOnSquare( square: Square): Set < PlayingPiece > {
        return this.pieceSet.getPiecesOnSquare( square )
    }

    fun squarePlusRoll(square: Square, diceRoll: DiceRoll ): Square {
        val squareIndex = this.squareList.indexOf( square )
        val newIndex = squareIndex + diceRoll.result
        return this.squareList[ newIndex.rem( this.squareList.size ) ]
    }

    fun startingSquare(): Square {
        return this.squareList.first()
    }

    // Housekeeping

    override fun equals( other: Any? ): Boolean {
        return other != null && this::class == other::class
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + squareList.hashCode()
        return result
    }

}