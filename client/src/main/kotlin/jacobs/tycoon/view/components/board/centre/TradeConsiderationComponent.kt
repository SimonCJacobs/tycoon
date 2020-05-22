package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.VNode

class TradeConsiderationComponent(
    private val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
)
    : CentreCellComponent() {

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            children (
                m( Tag.h5 ) { content( "Do you accept?" ) },
                buttonWithHandler( "Accept" ) { dealController.dealResponse( true ) },
                buttonWithHandler( "Reject" ) { dealController.dealResponse( false ) }
            )
        }
    }

    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {

        }
    }

    private fun buttonWithHandler( text: String, clickHandler: () -> Unit ): VNode {
        return m( Tag.button ) {
            content( text )
            eventHandlers {
                onclick = { clickHandler() }
            }
        }
    }

}
