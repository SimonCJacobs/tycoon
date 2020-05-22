package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.tycoon.clientcontroller.DealController

class TradeConsiderationHoldingComponent(
    private val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
)
    : CentreCellComponent() {

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        val offer = dealController.getOfferThatHasBeenMade()
        return {
            content( "Hang on a moment. ${ offer.offerRecipient } is considering an offer from " +
                "${ offer.offeringPlayer }" )
        }
    }

    @Suppress( "unused" )
    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {
            val fontSize = "40px"
        }
    }

}
