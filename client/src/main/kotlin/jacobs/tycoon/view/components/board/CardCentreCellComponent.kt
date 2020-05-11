package jacobs.tycoon.view.components.board

import jacobs.tycoon.domain.board.cards.Card

@Suppress("UNUSED_VARIABLE")
class CardCentreCellComponent(
    private val card: Card,
    squaresToASideExcludingCorners: Int
) : CentreCellComponent( squaresToASideExcludingCorners ) {

    override fun getFontSize(): String? {
        return "20px"
    }

    override fun getFontStyle(): String? {
        return null
    }

    override fun getText(): String {
        return this.card.instruction
    }

    override fun getTransform(): String? {
        return null
    }

}