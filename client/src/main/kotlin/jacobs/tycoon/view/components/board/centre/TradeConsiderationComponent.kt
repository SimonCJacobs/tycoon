package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.tycoon.clientcontroller.DealController

class TradeConsiderationComponent(
    private val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
)
    : CentreCellComponent() {

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            content( "Trade consideration" )
        }
    }

    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {

        }
    }

}
