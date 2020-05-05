package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.Game
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    private val index: Int
) : Comparable < Position > {
    companion object {
        val UNINITIALISED = Position( -1 )
    }

    fun getPlayer( game: Game ): Player {
        return game.players.getPlayerAtPosition( this )
    }

    override fun compareTo( other: Position ): Int {
        return this.index - other.index
    }
}