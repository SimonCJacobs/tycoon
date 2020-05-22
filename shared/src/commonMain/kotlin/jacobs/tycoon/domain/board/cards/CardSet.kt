package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.Square
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Cards must be shuffled before use
 */
@Serializable
abstract class CardSet {

    protected abstract val cardContentsList: List < Pair < String, CardAction > >

    @Transient private var cardList: List < Card > = emptyList()
    protected abstract val getOutOfJailFreeCardCount: Int
    @Transient private var shuffleOrder: ArrayDeque < Int > = ArrayDeque()

    fun applyShuffleOrder( order: List < Int > ) {
        this.populateCardList()
        this.shuffleOrder = ArrayDeque( order )
    }

    fun drawCard(): Card {
        return this.cardList[ this.shuffleOrder[ 0 ] ]
            .also { this.dealWithWhereaboutsOfCardAfterTurn( it ) }
    }

    /**
     * Included to help testing
     */
    fun pullCardOfIndexToFrontOfDeck( indexOfTargetCardInOriginalOrder: Int ) {
        val shuffleIndex = this.shuffleOrder.indexOf( indexOfTargetCardInOriginalOrder )
        this.shuffleOrder.removeAt( shuffleIndex )
            .also { this.shuffleOrder.addFirst( it ) }
    }

    fun returnToDeck( card: GetOutOfJailFreeCard ) {
        this.shuffleOrder.add( card.indexInDeck )
    }

    fun shuffle(): List < Int > {
        this.populateCardList()
        this.shuffleOrder = ArrayDeque( this.cardList.indices.shuffled() )
        return this.shuffleOrder
    }

    private fun dealWithWhereaboutsOfCardAfterTurn( card: Card ) {
        val cardTaken = this.shuffleOrder.removeFirst()
        if ( false == card.isRetainedByPlayer )
            this.shuffleOrder.addLast( cardTaken )
    }

    private fun populateCardList() {
        this.cardList = listOf( this.getRegularCards(), this.getGetOutOfJailFreeCards() )
            .flatten()
    }

    private fun getGetOutOfJailFreeCards(): List < GetOutOfJailFreeCard > {
        return ( 0 until getOutOfJailFreeCardCount ).map {
            GetOutOfJailFreeCard( it, this )
        }
    }

    private fun getRegularCards(): List < RegularCard > {
        return cardContentsList.map { contents ->
            RegularCard(
                instruction = contents.first,
                action = contents.second
            )
        }
    }

    // PROTECTED API

    protected fun advanceTo( text: String, square: Square ): Pair < String, CardAction > {
        return text to { player -> movingAPiece( player, square ) }
    }

    protected fun goBackTo( text: String, square: Square ): Pair < String, CardAction > {
        return text to { player -> movingAPieceNotViaGo( player, square ) }
    }

    protected fun goToJail( jailSquare: JailSquare, goSquare: GoSquare, goCreditAmount: CurrencyAmount )
            : Pair < String, CardAction > {
        return "Go to jail. Move directly to jail. Do not pass \"${ goSquare.name }\"." +
            "Do not collect $goCreditAmount" to
                 { player -> movingAPieceNotViaGo( player, jailSquare ) }
    }

    protected fun payMoneyAction( amount: CurrencyAmount, reason: String, textLambda: ( CurrencyAmount ) -> String )
            : Pair < String, CardAction > {
        return textLambda( amount ) to { player -> paymentDueToBank( player, player, reason, amount ) }
    }

    protected fun receiveMoneyAction( amount: CurrencyAmount, textLambda: ( CurrencyAmount ) -> String )
            : Pair < String, CardAction > {
        return textLambda( amount ) to { player -> acceptFunds( player, amount ) }
    }

    protected fun streetRepairs( houseFee: CurrencyAmount, hotelFee: CurrencyAmount,
            textLambda: ( Pair < CurrencyAmount, CurrencyAmount > ) -> String ): Pair < String, CardAction > {
        return textLambda( houseFee to hotelFee ) to
            { player -> streetRepairs( player, houseFee, hotelFee ) }
    }

}