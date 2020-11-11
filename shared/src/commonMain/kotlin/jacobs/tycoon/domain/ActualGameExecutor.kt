package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.BadActionException

/**
 * Essentially providing a single entry point to the domain and state so that this can be restricted to one
 * coroutine at a time
 */
class ActualGameExecutor : GameExecutor {

    private lateinit var gameController: GameController

    fun registerGameController( gameController: GameController ) {
        this.gameController = gameController
    }

    override suspend fun execute( action: GameAction ) {
        try { action.execute( this.gameController ) }
        catch ( e: BadActionException ) {
            println( "Bad action requested: ${ e.message }" )
        }
    }

    override suspend fun startGame() {
        this.gameController.startGame()
    }

}