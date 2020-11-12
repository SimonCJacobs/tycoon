package jacobs.tycoon.view.components.players

import jacobs.mithril.MouseEventHandler
import jacobs.mithril.Tag
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

    override fun getAcceptFundsDisplay(): VNode {
        return this.getButtonWithTextAndHandler( "Accept funds" ) {
            playerActionController.acceptFunds()
        }
    }

    override fun getDiceRollDisplay(): VNode {
        return this.getButtonWithTextAndHandler( "Roll that dice!" ) {
            playerActionController.rollTheDice()
        }
    }

    override fun getChargeRentDisplay(): List < VNode > {
        return playerActionController.mapOnPropertiesCanChargeRent { property ->
            getButtonWithTextAndHandler( "Charge rent for $property" ) {
                playerActionController.chargeRent( property )
            }
        }
    }

    override fun getJailEscapeDisplay(): VNode {
        return m( Tag.div ) {
            children(
                getButtonWithTextAndHandler(
                    "Pay ${ playerActionController.getJailFine() } fine",
                    disabled = ( false == gameState.game().canPlayerPayJailFine( player ) )
                ) {
                    playerActionController.payJailFine()
                },
                getPotentialGetOutOfJailFreeCardButton()
            )
        }
    }

    private fun getPotentialGetOutOfJailFreeCardButton(): VNode? {
        if ( this.playerActionController.doesPlayerHaveGetOutOfJailFreeCard() )
            return  getButtonWithTextAndHandler( "Use Get Out of Jail Free Card" ) {
                playerActionController.useGetOutOfJailFreeCard()
            }
        else
            return null
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
        return getButtonWithTextAndHandler( "Read card" ) { playerActionController.readCard() }
    }

    override fun getPayFineOrTakeChance(): VNode {
        return m( Tag.div ) {
            children(
                getButtonWithTextAndHandler( "Pay fine" ) { playerActionController.payFineNotTakeChance() },
                getButtonWithTextAndHandler( "Take chance" ) { playerActionController.takeChance() }
            )
        }
    }

    override fun getActionsKeptToSelf(): Array < VNode? > {
        return arrayOf(
            this.getDealingOption()
        )
    }

    override fun getBillToPayDisplay(): VNode {
        return getButtonWithTextAndHandler(
            "Pay ${ playerActionController.getBillReason() } ${ playerActionController.getBillAmount() }"
        ) { playerActionController.attemptToPay() }
    }

    private fun getDealingOption(): VNode {
        if ( playerActionController.isPlayerComposingDeal() )
            return getButtonWithTextAndHandler( "Hide dealing" ) { playerActionController.hideDealingCell() }
        else
            return getButtonWithTextAndHandler( "Do dealing" ) { playerActionController.startComposingDeal() }
    }

    @Suppress( "unused" )
    private fun getButtonWithTextAndHandler(
        text: String,
        disabled: Boolean = false,
        handler: MouseEventHandler
    ): VNode {
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
