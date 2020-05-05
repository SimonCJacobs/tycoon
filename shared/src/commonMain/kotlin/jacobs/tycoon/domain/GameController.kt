package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.RollForOrder
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

@Suppress( "DeferredIsResult" )
class GameController( kodein: Kodein ) : GameExecutor {

    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameState by kodein.instance < GameState > ()

    suspend fun duplicate( action: GameAction ) {
        action.duplicate( gameState )
    }

    override suspend fun execute( action: GameAction ) {
        action.execute( gameState )
    }

    override suspend fun startGame() {
        this.gameState.setGame( this.gameFactory.newGame() )
    }

}