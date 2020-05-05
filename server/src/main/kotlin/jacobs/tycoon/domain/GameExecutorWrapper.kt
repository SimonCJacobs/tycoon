package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.sync.Mutex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@Suppress( "UNCHECKED_CAST", "DeferredIsResult" )
class GameExecutorWrapper( kodein: Kodein ) : GameExecutor {

    private val gameExecutor by kodein.instance < GameExecutor > ( tag = "actual" )
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    override suspend fun execute( action: GameAction ) {
        this.mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
        this.gameExecutor.execute( action )
        this.gameHistory.logAction( action )
        this.mutex.unlock()
    }

    override suspend fun startGame() {
        this.gameExecutor.startGame()
        this.execute( NewGame() )
    }

}