package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.cards.CardSet
import jacobs.tycoon.domain.board.cards.ShuffleOrders
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.rules.MiscellaneousRules
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class Board {

    abstract val location: String
    @Transient lateinit var pieceSet: PieceSet
    abstract var squareList: List < Square > protected set

    abstract val currency: Currency

    abstract var goSquare: GoSquare protected set
    abstract var jailSquare: JailSquare protected set
    abstract var maryleboneStation: Station protected set
    abstract var mayfair: Street protected set
    abstract var oldKentRoad: Street protected set
    abstract var pallMall: Street protected set
    abstract var trafalgarSquare: Street protected set

    protected abstract val cardSets: Map < String, CardSet >

    // Initialisation

    /**
     * Requires initialising and shuffling before use. Reason for doing this in a method rather than on construction is
     * to sidestep some anomalies that arise with the order of construction when deserializing.
     */
    abstract fun initialise( gameRules: MiscellaneousRules )

    fun applyShuffleOrders( shuffleOrders: ShuffleOrders ): ShuffleOrders {
        shuffleOrders.forEach {
            cardSets.getValue( it.key ).applyShuffleOrder( it.value )
        }
        return shuffleOrders
    }

    fun shuffleCards(): ShuffleOrders {
        return cardSets.mapValues { it.value.shuffle() }
    }

    // Protected API

    protected fun Int.toCurrency(): CurrencyAmount {
        return currency.ofAmount( this )
    }

    // Public API

    fun cardSetNames(): Set < String > {
        return cardSets.keys
    }

    inline fun < reified T : Square > getActualSquare( squareCopy: T ): T {
        return squareList.find { actualSquare -> actualSquare == squareCopy } as T
    }

    inline fun < reified T : Square > getActualSquares( squareCollection: Collection < T > ): List < T > {
        return squareCollection.map { squareCopy ->
            squareList.find { actualSquare -> actualSquare == squareCopy } as T
        }
    }

    fun getNamedCardSet( name: String ): CardSet {
        return this.cardSets.getValue( name )
    }

    fun getPieceCount(): Int {
        return this.pieceSet.count()
    }

    fun getPiecesOnSquare( square: Square): Set < PlayingPiece > {
        return this.pieceSet.getPiecesOnSquare( square )
    }

    fun squareMinusSpaces( existingSquare: Square, numberOfSpaces: Int ): Square {
        return squarePlusSpaces( existingSquare, squareList.size - numberOfSpaces )
    }

    fun squarePlusRoll( square: Square, diceRoll: DiceRoll ): Square {
        return squarePlusSpaces( square, diceRoll.result )
    }

    fun startingSquare(): Square {
        return this.squareList.first()
    }

    private fun squarePlusSpaces( square: Square, spaceCount: Int ): Square {
        val newIndex = square.indexOnBoard + spaceCount
        return this.squareList[ newIndex.rem( this.squareList.size ) ]
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