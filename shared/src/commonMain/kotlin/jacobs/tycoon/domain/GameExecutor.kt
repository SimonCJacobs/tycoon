package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction

interface GameExecutor {
    suspend fun execute( action: GameAction ): GameAction
}