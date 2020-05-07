package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.Game
import kotlinx.serialization.Serializable

@Serializable
data class SeatingPosition (
    private val index: Int
) : Comparable < SeatingPosition > {
    companion object {
        val UNINITIALISED = SeatingPosition( -1 )
    }

    fun getPlayer( game: Game ): Player {
        return game.players.getPlayerAtPosition( this )
    }

    override fun compareTo( other: SeatingPosition ): Int {
        return this.index - other.index
    }
}