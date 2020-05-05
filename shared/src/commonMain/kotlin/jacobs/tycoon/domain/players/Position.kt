package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.Game
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    private val index: Int
) {
    companion object {
        val UNINITALISED = Position( -1 )
    }

    fun getPlayer( game: Game ): Player {
        return game.players.getPlayerAtPosition( this )
    }
}