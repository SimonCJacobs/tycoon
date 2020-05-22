package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.DealController
import org.js.mithril.VNode

class BuildingComponent(
    override val dealController: DealController,
    override var squaresToASideExcludingCorners: Int
) : ComposingDealComponent() {

    private val buildingForm: BuildingForm = BuildingForm( dealController  )

    override fun extraDisplay(): VNode {
        return m( buildingForm )
    }

    @Suppress( "unused" )
    override fun getDoTheDealButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { dealController.buildHouses() }
            }
            content( "Build" )
        }
    }

}