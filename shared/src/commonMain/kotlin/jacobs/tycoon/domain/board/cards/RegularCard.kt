package jacobs.tycoon.domain.board.cards

class RegularCard(
    override val instruction: String,
    override val action: CardAction
) : Card {
    override val isRetainedByPlayer: Boolean
        get() = false
}