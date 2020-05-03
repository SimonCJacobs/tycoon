package jacobs.tycoon.view.components.players

import jacobs.jsutilities.jsObject
import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.domain.players.Player
import org.js.mithril.VNode

class MultiplePlayersComponent(
    private val playerToExclude: Player,
    private val playerComponentFactory: PlayerComponentFactory
) : Component {

    override fun view(): VNode {
        return m( Tag.div ) {
            attributes {
                style = jsObject {
                    display = "flex"
                    justifyContent = "space-around"
                }
            }
            children(
                playerComponentFactory.getSinglePlayerComponentsExcluding( playerToExclude )
                    .map { m( it ) }
            )
        }
    }

}
