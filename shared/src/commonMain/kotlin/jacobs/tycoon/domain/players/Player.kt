package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.board.cards.GetOutOfJailFreeCard
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

    @Transient private var getOutOfJailFreeCards: MutableList < GetOutOfJailFreeCard > = mutableListOf()
    @Transient private val locationWhenRolledRecord: MutableList < Square > = mutableListOf()
    @Transient private val properties: MutableSet < Property > = mutableSetOf()
    @Transient private val rollCountWhenPenalised: MutableList < Int > = mutableListOf()
    @Transient private val rollRecord: MutableList < DiceRoll > = mutableListOf()

    fun acquireGetOutOfJailFreeCard( card: GetOutOfJailFreeCard ) {
        this.getOutOfJailFreeCards.add( card )
    }

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

    fun canTrade( property: Property ): Boolean {
        return this.owns( property ) &&
            ( false == property is Street || false == this.hasAnyDevelopmentInColourGroup( property.colourGroup ) )
    }

    fun creditFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings + amount
    }

    fun debitFunds( amount: CurrencyAmount ) {
        this.cashHoldings = this.cashHoldings - amount
    }

    fun disposeOfGetOutOfJailFreeCards( number: Int ): List < GetOutOfJailFreeCard > {
        return ( 1 .. number ).map { removeGetOutOfJailFreeCard() }
    }

    fun disposeOfProperty( property: Property ) {
        this.properties.remove( property )
    }

    fun forEachPropertyOwned( callback: ( Property ) -> Unit ) {
        this.properties.forEach( callback )
    }

    fun getAllAssets(): Assets {
        return Assets(
            this.properties.toList(),
            this.cashHoldings,
            this.getOutOfJailFreeCards.size
        )
    }

    fun hasGetOutOfJailFreeCard(): Boolean {
        return this.getOutOfJailFreeCards.isNotEmpty()
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

    fun howManyGetOutOfJailFreeCards(): Int {
        return this.getOutOfJailFreeCards.size
    }

    fun howManyStationsOwned(): Int {
        return this.properties.count { it is Station }
    }

    fun howManyUtilitiesOwned(): Int {
        return this.properties.count { it is Utility }
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

    fun isStreetOwnedInAFullColourGroup( street: Street ): Boolean {
        return street.colourGroup.numberInGroup ==
           this.howManyOfColourGroupOwned( street.colourGroup )
    }

    fun justRolledADouble(): Boolean {
        return this.lastDiceRoll().isDouble()
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

    fun ownsAnyProperty(): Boolean {
        return this.properties.size > 0
    }

    fun penalisedForDoubleThrowing() {
        this.rollCountWhenPenalised.add( this.rollRecord.size )
    }

    fun returnGetOutOfJailFreeCard() {
        this.removeGetOutOfJailFreeCard()
            .returnToDeck()
    }

    fun totalNumberOfHouses(): Int {
        return this.streets().sumBy {
            it.getNumberOfHousesExcludingHotels()
        }
    }

    fun totalNumberOfHotels(): Int {
        return this.streets().count { it.hasHotel() }
    }

    // PRIVATE METHODS

    private fun colourGroup( colourGroup: ColourGroup ): Set < Street > {
        return this.streets().filter { it.colourGroup == colourGroup }.toSet()
    }

    private fun hasAnyDevelopmentInColourGroup( colourGroup: ColourGroup ): Boolean {
        return this.colourGroup( colourGroup ).any { it.hasAnyDevelopment() }
    }

    private fun howManyOfColourGroupOwned( colourGroup: ColourGroup): Int {
        return this.colourGroup( colourGroup ).size
    }

    /**
     * Arbitrary decision to go last-in-first-out here
     */
    private fun removeGetOutOfJailFreeCard(): GetOutOfJailFreeCard {
        return this.getOutOfJailFreeCards.removeFirst()
    }

    private fun streets(): List < Street > {
        return this.properties.filterIsInstance < Street >()
    }

    override fun equals( other: Any? ): Boolean {
        return other != null &&
            other is Player &&
            other.name == this.name
    }

    override fun compareTo( other: Player ): Int {
        return this.position.compareTo( other.position )
    }

    override fun toString(): String {
        return this.name
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

}