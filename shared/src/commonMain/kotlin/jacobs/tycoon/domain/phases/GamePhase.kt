package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.Game

abstract class GamePhase {

    abstract fun nextPhase( game: Game ): GamePhase

    override fun equals( other: Any? ): Boolean {
        if ( other == null )
            return false
        else
            return this::class == other::class
    }

    override fun hashCode(): Int {
        return this::class.simpleName.hashCode()
    }
}