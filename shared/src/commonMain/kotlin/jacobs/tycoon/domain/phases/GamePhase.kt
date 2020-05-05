package jacobs.tycoon.domain.phases

abstract class GamePhase {
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