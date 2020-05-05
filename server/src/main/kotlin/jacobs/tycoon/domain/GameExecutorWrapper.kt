package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.state.GameHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@Suppress( "UNCHECKED_CAST", "DeferredIsResult" )
class GameExecutorWrapper( kodein: Kodein ) : GameExecutor {

    private val gameExecutor by kodein.instance < GameExecutor > ( tag = "actual" )
    private val gameHistory by kodein.instance < GameHistory > ()

    private val mutex = Mutex()

    override suspend fun execute( action: GameAction ): GameAction {
        mutex.lock() // Lock the update to ensure order of calls is preserved as this can be accessed simultaneously
        val resultingAction = gameExecutor.execute( action )
        gameHistory.logAction( resultingAction )
        mutex.unlock()
        return resultingAction
    }

}