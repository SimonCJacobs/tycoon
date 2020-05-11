package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.domain.players.Player
import org.js.mithril.VNode

class MultiplePlayersComponent(
    private val playerToExclude: Player,
    private val playerComponentFactory: PlayerComponentFactory
) : Component {

    @Suppress( "unused" )
    override fun view(): VNode {
        return m( Tag.div ) {
            attributes ( object {
                val style = object {
                    val display = "flex"
                    val justifyContent = "space-around"
                }
            } )
            children(
                playerComponentFactory.getOrderedSinglePlayerComponentsExcluding( playerToExclude )
                    .map { m( it ) }
            )
        }
    }

}
