package jacobs.tycoon.state

import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.services.ActionProcessor

class GameHistory {

    private val gameActions: MutableList < GameAction > = mutableListOf()

    /**
     * Obtain a list of updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    fun getActionsBetween( startIndex: Int, endIndex: Int ): GameActionCollection {
        return GameActionCollection.fromList(
            this.gameActions.subList( startIndex, endIndex )
        )
    }

    fun getActionCount(): Int {
        return gameActions.size
    }

    fun logAction( gameAction: GameAction ) {
        this.gameActions.add( gameAction )
    }

    fun < T : Any > processAllLogs( processor: ActionProcessor < T > ): List < T > {
        return this.gameActions.let { processSubList( processor, it ) }
    }

    fun < T : Any > processHistoryFromIndex(processor: ActionProcessor < T >, startIndex: Int ): List < T > {
        val updateCount = this.getActionCount()
        if ( startIndex >= updateCount )
            return emptyList()
        else
            return this.processLogsBetween( processor, startIndex, updateCount )
    }

    /**
     * Process updates to the game state between [startIndex] (inclusive) and [endIndex] (exclusive).
     */
    private fun < T : Any > processLogsBetween( processor: ActionProcessor < T >, startIndex: Int, endIndex: Int ): List < T > {
        return this.gameActions.subList( startIndex, endIndex )
            .let { processSubList( processor, it ) }
    }

    private fun < T : Any > processSubList( processor: ActionProcessor < T >, subList: List < GameAction > ): List < T > {
        return subList.mapNotNull( processor::process )
    }

}