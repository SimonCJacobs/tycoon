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

    override fun getOwnTurnDiceRollDisplay(): VNode {
        return m( Tag.p ) {
            attributes {
                fontStyle = "italic"
            }
            content( "Turn to roll" )
        }
    }

}