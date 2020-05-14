package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * Essentially providing a single entry point to the domain and state so that this can be restricted to one
 * coroutine at a time
 */
class ActualGameExecutor : GameExecutor {

    private lateinit var gameController: GameController

    fun registerGameController( gameController: GameController ) {
        this.gameController = gameController
    }

    override suspend fun execute( action: GameAction) {
        action.execute( this.gameController )
    }

    override suspend fun startGame() {
        this.gameController.startGame()
    }

}