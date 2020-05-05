package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.PlayerActionController
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import org.js.mithril.VNode

class ActiveSinglePlayerComponent(
    override val gameState: GameState,
    override val playerActionController: PlayerActionController,
    override val player: Player
) : SinglePlayerComponent() {

    override fun getOwnTurnDiceRollDisplay(): VNode {
        return m( Tag.button ) {
            attributes {
                type = "button"
            }
            eventHandlers {
                onclick = { playerActionController.rollTheDice() }
            }
            content( "Roll that dice!" )
        }
    }

}
