package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.js.mithril.VNode

class PassiveSinglePlayerComponent(
    override val gameState: GameState,
    override val pieceDisplayStrategy: PieceDisplayStrategy,
    override val playerActionController: PlayerActionController,
    override val player: Player
) : SinglePlayerComponent() {

    override fun getAcceptFundsDisplay(): VNode {
        return this.getTextUpdate( "Accepting funds" )
    }

    override fun getBillToPayDisplay(): VNode {
        return this.getTextUpdate( "Paying bill" )
    }

    override fun getDiceRollDisplay(): VNode {
        return this.getTextUpdate( "Turn to roll" )
    }

    override fun getJailEscapeDisplay(): VNode {
        return this.getTextUpdate( "Considering jail options" )
    }

    override fun getChargeRentDisplay(): List < VNode > {
        return emptyList()
    }

    override fun getPropertyPurchaseDisplay(): VNode {
        return this.getTextUpdate( "Considering purchase" )
    }

    override fun getReadCardDisplay(): VNode {
        return this.getTextUpdate( "Reading card" )
    }

    override fun getPayFineOrTakeChance(): VNode? {
        return this.getTextUpdate( "Deciding whether to pay or take chance" )
    }

    override fun getActionsKeptToSelf(): Array < VNode? > {
        return arrayOf() // No actions for the passive player of course!
    }

    @Suppress( "unused" )
    private fun getTextUpdate(text: String ): VNode {
        return m( Tag.p ) {
            attributes ( object {
                val fontStyle = "italic"
            } )
            content( text )
        }
    }

}
