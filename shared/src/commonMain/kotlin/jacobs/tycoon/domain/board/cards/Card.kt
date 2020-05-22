package jacobs.tycoon.domain.board.cards

interface Card {
    val instruction: String
    val action: CardAction
    val isRetainedByPlayer: Boolean
}