package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.VNode

class ComposingDealComponent : CentreCellComponent() {

    lateinit var dealController: DealController
    override var squaresToASideExcludingCorners: Int = -1

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            children(
                getCancelButton()
            )
        }
    }

    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {

        }
    }

    @Suppress( "unused" )
    private fun getCancelButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { dealController.cancelComposingDeal() }
            }
            content( "Cancel" )
        }
    }

}
