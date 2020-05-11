package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.colourgroups.ColourGroup
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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

    @Transient private val properties: MutableSet < Property > = mutableSetOf()
    @Transient private val rollRecord: MutableList < DiceRoll > = mutableListOf()

    fun addDiceRoll( diceRoll: DiceRoll ) {
        this.rollRecord.add( diceRoll )
    }

    fun buyAtListPrice( property: Property ) {
        this.debitFunds( property.listPrice )
        this.properties.add( property )
    }

    fun creditFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings + amount
    }

    fun debitFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings - amount
    }

    fun howManyOfColourGroupOwned( colourGroup: ColourGroup): Int {
        return this.properties.count { it is Street && it.colourGroup == colourGroup }
    }

    fun howManyStationsOwned(): Int {
        return this.properties.count { it is Station }
    }

    fun howManyUtilitiesOwned(): Int {
        return this.properties.count { it is Utility }
    }

    fun hasRolledMaximumDoublesInARow(): Boolean {
        return this.rollRecord.size >= toJailDoubleCount &&
            this.rollRecord.subList( this.rollRecord.size - toJailDoubleCount, this.rollRecord.size )
                .all { it.isDouble() }
    }

    fun isPosition( otherPosition: SeatingPosition ): Boolean {
        return this.position == otherPosition
    }

    fun lastDiceRoll(): DiceRoll {
        return this.rollRecord.last()
    }

    fun moveToSquareAndMaybePassGo( newSquare: Square ): Boolean {
        val didPassGo = newSquare.indexOnBoard < this.piece.square.indexOnBoard
        this.piece.moveToSquare( newSquare )
        return didPassGo
    }

    fun owns( property: Property ): Boolean {
        return this.properties.contains( property )
    }

    fun square(): Square {
        return this.piece.square
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }

}