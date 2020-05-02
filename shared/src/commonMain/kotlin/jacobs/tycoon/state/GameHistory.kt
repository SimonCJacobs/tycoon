package jacobs.tycoon.state

class GameHistory {

    private val gameUpdates: MutableList < GameUpdate > = mutableListOf()

    /**
     * Obtain a list of updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    fun getUpdatesBetween( startIndex: Int, endIndex: Int ): GameUpdateCollection {
        return GameUpdateCollection.fromList(
            this.gameUpdates.subList( startIndex, endIndex )
        )
    }

    fun getUpdateCount(): Int {
        return gameUpdates.size
    }

    fun logUpdate( gameUpdate: GameUpdate ) {
        this.gameUpdates.add( gameUpdate )
    }

    /**
     * Process updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    fun < T > processLogsBetween(processor: UpdateProcessor < T >, startIndex: Int, endIndex: Int ): List < T > {
        return this.gameUpdates.subList( startIndex, endIndex )
            .map( processor::process )
    }

}