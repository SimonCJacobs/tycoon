package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.Game
import jacobs.websockets.content.MessageContent
import jacobs.websockets.content.Messageable
import kotlinx.serialization.Serializable

@Serializable
data class SeatingPosition (
    val index: Int
) : Comparable < SeatingPosition >, Messageable, MessageContent {

    companion object {
        val UNASSIGNABLE = SeatingPosition( -1 )
    }

    fun getPlayer( game: Game ): Player {
        return game.players.getPlayerAtPosition( this )
    }

    fun isUnassigned(): Boolean {
        return this == UNASSIGNABLE
    }

    override fun compareTo( other: SeatingPosition ): Int {
        return this.index - other.index
    }

    override fun toMessageContent(): MessageContent {
        return this
    }

    override fun equals( other: Any? ): Boolean {
        return other != null && other is SeatingPosition &&
            this.index == other.index
    }

}