package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.cards.Card
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
    var cashHoldings: CurrencyAmount
) : Comparable < Player > {

    companion object {
        val NULL = Player(
            name = "",
            piece = PlayingPiece.NULL,
            position = SeatingPosition.UNINITIALISED,
            cashHoldings = CurrencyAmount.NULL
        )
    }

    @Transient private var getOutOfJailFreeCards: MutableList < Card > = mutableListOf()
    @Transient private val locationWhenRolledRecord: MutableList < Square > = mutableListOf()
    @Transient private val properties: MutableSet < Property > = mutableSetOf()
    @Transient private val rollCountWhenPenalised: MutableList < Int > = mutableListOf()
    @Transient private val rollRecord: MutableList < DiceRoll > = mutableListOf()

    fun acquireProperty( property: Property ) {
        this.properties.add( property )
    }

    fun addDiceRoll( diceRoll: DiceRoll ) {
        this.rollRecord.add( diceRoll )
        this.locationWhenRolledRecord.add( this.location() )
    }

    fun buyAtListPrice( property: Property ) {
        this.debitFunds( property.listPrice )
        this.acquireProperty( property )
    }

    fun creditFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings + amount
    }

    fun debitFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings - amount
    }

    fun disposeOfGetOutOfJailFreeCards( number: Int ): List < Card > {
        return ( 1 .. number ).map { removeGetOutOfJailFreeCard() }
    }

    fun disposeOfProperty( property: Property ) {
        this.properties.remove( property )
    }

    fun hasGetOutOfJailFreeCard(): Boolean {
        return this.getOutOfJailFreeCards.isNotEmpty()
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

    fun howManyDoublesRolledInRow(): Int {
        var count = 0
        this.rollRecord.reversed().forEach {
            if ( it.isDouble() )
                count++
            else
                return count
        }
        return count
    }

    fun howManyRollsSinceLastPenalisedForDoubles(): Int? {
        return this.rollCountWhenPenalised.lastOrNull()
            ?.let { lastCount -> this.rollRecord.size - lastCount  }
    }

    fun howManyRollsHadOnCurrentSquare(): Int {
        var count = 0
        this.locationWhenRolledRecord.reversed().forEach {
            if ( location() == it )
                count++
            else
                return count
        }
        return count
    }

    fun isSittingAtPosition( otherPosition: SeatingPosition ): Boolean {
        return this.position == otherPosition
    }

    fun lastDiceRoll(): DiceRoll {
        return this.rollRecord.last()
    }

    fun location(): Square {
        return this.piece.square
    }

    fun moveToSquareAndMaybePassGo( newSquare: Square ): Boolean {
        val didPassGo = newSquare.indexOnBoard < this.piece.square.indexOnBoard
        this.piece.moveToSquare( newSquare )
        return didPassGo
    }

    fun owns( property: Property ): Boolean {
        return this.properties.contains( property )
    }

    fun penalisedForDoubleThrowing() {
        this.rollCountWhenPenalised.add( this.rollRecord.size )
    }

    fun returnGetOutOfJailFreeCard() {
        this.removeGetOutOfJailFreeCard()
            .returnToDeck()
    }

    /**
     * Arbitrary decision to go last-in-first-out here
     */
    private fun removeGetOutOfJailFreeCard(): Card {
        return this.getOutOfJailFreeCards.removeFirst()
    }

    fun saveGetOutOfJailFreeCard( card: Card ) {
        this.getOutOfJailFreeCards.add( card )
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }

}