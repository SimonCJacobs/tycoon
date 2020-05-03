package jacobs.tycoon.view.components.players

import jacobs.mithril.Tag
import org.js.mithril.Component
import jacobs.mithril.m
import jacobs.tycoon.domain.players.Player
import org.js.mithril.VNode

class SinglePlayerComponent(
    private val player: Player
) : Component {

    override fun view(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.h4 ) { content( player.name ) },
                m( Tag.h6 ) { content( player.piece.name ) }
            )
        }
    }

}
