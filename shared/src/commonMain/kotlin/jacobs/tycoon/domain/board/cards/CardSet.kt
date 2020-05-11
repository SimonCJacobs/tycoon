package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Cards must be shuffled before use
 */
@Serializable
abstract class CardSet {

    protected abstract val cardList: List < Card >

    @Transient private var nextCardPointer: Int = 0
    @Transient private var shuffleOrder: List < Int > = emptyList()

    fun applyShuffleOrder( order: List < Int > ) {
        this.shuffleOrder = order
    }

    fun drawCard(): Card {
        val indexInShuffleOrder = nextCardPointer++.rem( cardList.size )
        return this.cardList[ this.shuffleOrder[ indexInShuffleOrder ] ]
    }

    fun shuffle(): List < Int > {
        this.shuffleOrder = this.cardList.indices.shuffled()
        return this.shuffleOrder
    }

}