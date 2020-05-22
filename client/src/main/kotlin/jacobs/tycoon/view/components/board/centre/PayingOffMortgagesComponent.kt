package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.VNode

class PayingOffMortgagesComponent(
    override val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
) : ComposingDealComponent() {

    override fun extraDisplay(): VNode? {
        return null
    }

    @Suppress( "unused" )
    override fun getDoTheDealButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { dealController.payOffMortgages() }
            }
            content( "Pay off" )
        }
    }

}