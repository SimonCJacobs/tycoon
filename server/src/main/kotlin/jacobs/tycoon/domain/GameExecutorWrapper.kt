package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.gameadmin.NewGame
import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@Suppress( "UNCHECKED_CAST", "DeferredIsResult" )
class GameExecutorWrapper( kodein: Kodein ) : GameExecutor {

    private val gameExecutor by kodein.instance < GameExecutor > ( tag = "actual" )
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    /**
     * A note on thread safety.
     *
     * All game actions triggering a modification of the state of the application come through this method.
     * Thus the lock placed on this code means that only one coroutine at a time can modify the state of the game
     * and the entire underlying game can execute synchronously and safe in the knowledge other coroutines will not
     * be changing state unexpectedly.
     */
    override suspend fun execute( action: GameAction ) {
        this.mutex.withLock { // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
            this.gameExecutor.execute( action )
            this.gameHistory.logAction( action )
        }
    }

    /**
     * This is a sole exception to the thread safety point! Callers must start the game before allow any
     * other actions to be executed
     */
    override suspend fun startGame() {
        this.gameExecutor.startGame()
        this.execute( NewGame() )
    }

}