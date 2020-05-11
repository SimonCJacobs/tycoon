package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import org.js.mithril.VNode

abstract class SinglePlayerComponent : Component {

    protected abstract val gameState: GameState
    protected abstract val pieceDisplayStrategy: PieceDisplayStrategy
    protected abstract val playerActionController: PlayerActionController
    protected abstract val player: Player

    override fun view(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.h4 ) {
                    content( player.name )
                },
                m( Tag.h5 ) {
                    content( player.cashHoldings.toString() )
                },
                m( Tag.h6 ) {
                    child( m( pieceDisplayStrategy.getPieceDisplayComponent( player.piece ) ) )
                },
                getActionArea()
            )
        }
    }

    private fun getActionArea(): VNode {
        return m( Tag.div ) {
            children(
                *getOwnTurnDisplays(),
                *getAlwaysActionsIfInGame()
            )
        }
    }

    private fun getOwnTurnDisplays(): Array < VNode? > {
        if ( playerActionController.isItOwnTurn() )
            return arrayOf(
                if ( playerActionController.isItTimeToRollTheDice() ) getDiceRollDisplay() else null,
                if ( playerActionController.isThereAChanceToBuyProperty() ) getPropertyPurchaseDisplay() else null,
                if ( playerActionController.isThereAChanceToChargeRent() ) getChargeRentDisplay() else null,
                if ( playerActionController.isThereACardToRead() ) getReadCardDisplay() else null
            )
        else
            return emptyArray()
    }

    private fun getAlwaysActionsIfInGame(): Array < VNode ? > {
        if ( playerActionController.isGameUnderway() )
            return getAlwaysActions()
        else
            return emptyArray()
    }

    protected abstract fun getChargeRentDisplay(): VNode?
    protected abstract fun getDiceRollDisplay(): VNode
    protected abstract fun getReadCardDisplay(): VNode
    protected abstract fun getPropertyPurchaseDisplay(): VNode
    protected abstract fun getAlwaysActions(): Array < VNode? >

}
