package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Cards must be shuffled before use
 */
@Serializable
abstract class CardSet {

    protected abstract val cardContentsList: List < Pair < String, CardAction > >

    @Transient private var cardList: List < Card > = emptyList()
    @Transient private var shuffleOrder: ArrayDeque < Int > = ArrayDeque()

    fun applyShuffleOrder( order: List < Int > ) {
        this.populateCardList()
        this.shuffleOrder = ArrayDeque( order )
    }

    fun drawCard(): Card {
        return this.cardList[ this.shuffleOrder[ 0 ] ]
            .also { this.dealWithWhereaboutsOfCardAfterTurn( it ) }
    }

    fun returnToDeck( card: Card ) {
        this.shuffleOrder.add( card.indexInDeck )
    }

    fun shuffle(): List < Int > {
        this.populateCardList()
        this.shuffleOrder = ArrayDeque( this.cardList.indices.shuffled() )
        return this.shuffleOrder
    }

    private fun dealWithWhereaboutsOfCardAfterTurn( card: Card ) {
        if ( card.isRetainedByPlayer )
            this.shuffleOrder.removeFirst()
        else
            this.shuffleOrder.addLast( this.shuffleOrder.removeFirst() )
    }

    private fun populateCardList() {
        this.cardList = cardContentsList.mapIndexed { index, contents ->
            Card(
                indexInDeck = index,
                instruction = contents.first,
                action = contents.second,
                cardSet = this
            )
        }
    }

}