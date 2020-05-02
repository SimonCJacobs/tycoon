package jacobs.tycoon.state

interface UpdateProcessor < T > {
    fun process( gameUpdate: GameUpdate ): T
}