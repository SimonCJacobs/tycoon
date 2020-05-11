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

    override fun getDiceRollDisplay(): VNode {
        return this.getTextUpdate( "Turn to roll" )
    }

    override fun getChargeRentDisplay(): VNode? {
        return null // Must keep silent!
    }

    override fun getPropertyPurchaseDisplay(): VNode {
        return this.getTextUpdate( "Considering purchase" )
    }

    override fun getReadCardDisplay(): VNode {
        return this.getTextUpdate( "Reading card" )
    }

    override fun getAlwaysActions(): Array < VNode? > {
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
