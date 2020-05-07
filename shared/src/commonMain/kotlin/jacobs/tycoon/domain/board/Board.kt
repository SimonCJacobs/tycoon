package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class Board {
    abstract val location: String
    @Transient
    lateinit var pieceSet: PieceSet
    abstract val squareList: List < Square >

    companion object {
        private val JAIL_SQUARE = JailSquare()
    }

    fun getJailSquare(): Square {
        return JAIL_SQUARE
    }

    fun getPieceCount(): Int {
        return this.pieceSet.count()
    }

    fun getPiecesOnSquare( square: Square ): Set < PlayingPiece > {
        return this.pieceSet.getPiecesOnSquare( square )
    }

    fun squarePlusRoll( square: Square, diceRoll: DiceRoll ): Square {
        val squareIndex = this.squareList.indexOf( square )
        val newIndex = squareIndex + diceRoll.result
        return this.squareList[ newIndex.rem( this.squareList.size ) ]
    }

    fun startingSquare(): Square {
        return this.squareList.first()
    }

    override fun equals( other: Any? ): Boolean {
        return other != null && this::class == other::class
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + squareList.hashCode()
        return result
    }

}