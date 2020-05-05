package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import org.js.mithril.VNode

abstract class SinglePlayerComponent : Component {

    protected abstract val gameState: GameState
    protected abstract val playerActionController: PlayerActionController
    protected abstract val player: Player

    override fun view(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.h4 ) { content( player.name ) },
                m( Tag.h6 ) { content( player.piece.name ) },
                diceRollDisplay()
            )
        }
    }

    private fun diceRollDisplay(): VNode? {
        if ( playerActionController.isItOwnTurn() )
            return this.getOwnTurnDiceRollDisplay()
        else
            return null
    }

    protected abstract fun getOwnTurnDiceRollDisplay(): VNode

}
