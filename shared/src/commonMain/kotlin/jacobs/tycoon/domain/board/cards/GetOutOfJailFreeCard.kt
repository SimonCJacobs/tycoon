package jacobs.tycoon.domain.board.cards

class GetOutOfJailFreeCard (
    val indexInDeck: Int,
    private val cardSet: CardSet
) : Card {

    override val instruction: String
        get() = "Get out of jail free. This card may be kept until needed or sold"
    override val action: CardAction
        get() = { player -> player.acquireGetOutOfJailFreeCard( this@GetOutOfJailFreeCard ); null }
    override val isRetainedByPlayer: Boolean
        get() = true

    fun returnToDeck() {
        cardSet.returnToDeck( this )
    }

}