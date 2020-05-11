package jacobs.tycoon.view.components.players

import jacobs.mithril.MouseEventHandler
import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.js.mithril.VNode

class ActiveSinglePlayerComponent(
    override val gameState: GameState,
    override val pieceDisplayStrategy: PieceDisplayStrategy,
    override val playerActionController: PlayerActionController,
    override val player: Player
) : SinglePlayerComponent() {

    override fun getDiceRollDisplay(): VNode {
        return this.getButtonWithTextAndHandler( "Roll that dice!" ) {
            playerActionController.rollTheDice()
        }
    }

    override fun getChargeRentDisplay(): VNode {
        return getButtonWithTextAndHandler( "Charge rent" ) { playerActionController.chargeRent() }
    }

    override fun getPropertyPurchaseDisplay(): VNode {
        return m( Tag.div ) {
            children(
                getButtonWithTextAndHandler(
                    "Buy for ${ playerActionController.getPropertyPrice() }",
                    false == playerActionController.canPlayerAffordProperty()
                ) {
                    playerActionController.buyProperty()
                },
                getButtonWithTextAndHandler( "Send to auction" ) {
                    playerActionController.sendForAuction()
                }
            )
        }
    }

    override fun getReadCardDisplay(): VNode {
        return getButtonWithTextAndHandler( "Read card") { playerActionController.readCard() }
    }

    override fun getAlwaysActions(): Array < VNode? > {
        return arrayOf(
            this.getTradingOption(),
            if ( this.playerActionController.canBuild() ) this.getBuildingOption() else null
        )
    }

    private fun getTradingOption(): VNode {
        return getButtonWithTextAndHandler( "Trade" ) { playerActionController.startTrade() }
    }

    private fun getBuildingOption(): VNode {
        return getButtonWithTextAndHandler( "Build" ) { playerActionController.startNewBuilding() }
    }

    @Suppress( "unused" )
    private fun getButtonWithTextAndHandler(text: String, disabled: Boolean = false, handler: MouseEventHandler ): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val disabled = disabled
                val type = "button"
            } )
            eventHandlers {
                onclick = handler
            }
            content( text )
        }
    }

}
