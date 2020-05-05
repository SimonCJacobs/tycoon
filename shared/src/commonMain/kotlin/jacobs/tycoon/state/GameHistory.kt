package jacobs.tycoon.state

import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.services.ActionProcessor

class GameHistory {

    private val gameActions: MutableList <GameAction> = mutableListOf()

    /**
     * Obtain a list of updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    fun getUpdatesBetween( startIndex: Int, endIndex: Int ): GameActionCollection {
        return GameActionCollection.fromList(
            this.gameActions.subList( startIndex, endIndex )
        )
    }

    fun getUpdateCount(): Int {
        return gameActions.size
    }

    fun logAction( gameAction: GameAction ) {
        this.gameActions.add( gameAction )
    }

    /**
     * Process updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    fun < T : Any > processLogsBetween( processor: ActionProcessor < T >, startIndex: Int, endIndex: Int ): List < T > {
        return this.gameActions.subList( startIndex, endIndex )
            .mapNotNull( processor::process )
    }

}