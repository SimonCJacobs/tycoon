package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.tycoon.domain.board.cards.Card

class CardCentreCellComponent(
    private val card: Card,
    override var squaresToASideExcludingCorners: Int
) : CentreCellComponent() {

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            content( card.instruction )
        }
    }

    @Suppress( "unused" )
    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {
            val fontSize = "30px"
        }
    }

}