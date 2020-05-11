package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.cards.CardSet
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.GoSquare
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
    @Transient lateinit var pieceSet: PieceSet
    @Transient var squareList: List < Square > = emptyList() // Initialized only for serialization purposes
        private set

    abstract var goSquare: GoSquare
    abstract var jailSquare: JailSquare

    protected abstract val cardSets: List < CardSet >
    protected abstract val currency: Currency


    // Initialisation

    /**
     * Requires initialising and shuffling before use. Reason for doing this in a method rather than on construction is
     * to sidestep some anomalies that arise with the order of construction when deserializing.
     */
    fun initialise() {
        this.squareList = this.buildSquareList()
    }

    fun applyShuffleOrders( shuffleOrders: List < List < Int > >  ) {
        shuffleOrders.forEachIndexed {
            index, list -> cardSets[ index ].applyShuffleOrder( list )
        }
    }

    fun shuffleCards(): List < List < Int > > {
        return cardSets.map { it.shuffle() }
    }

    // Protected API

    protected abstract fun buildSquareList(): List < Square >

    protected fun Int.toCurrency(): CurrencyAmount {
        return currency.ofAmount( this )
    }

    // Public API

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