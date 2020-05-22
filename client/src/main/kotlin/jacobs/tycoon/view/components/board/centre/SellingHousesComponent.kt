package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.VNode

class SellingHousesComponent(
    override val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
) : ComposingDealComponent() {

    private val sellingHousesForm: SellingHousesForm = SellingHousesForm( dealController )

    override fun extraDisplay(): VNode {
        return m( sellingHousesForm )
    }

    @Suppress( "unused" )
    override fun getDoTheDealButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { dealController.sellHouses() }
            }
            content( "Sell houses" )
        }
    }

}