package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.PlayerFactory
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameController( kodein: Kodein ) : GameExecutor {

    private val gameFactory by kodein.instance < GameFactory > ()
    private val gameState by kodein.instance < GameState > ()
    private val playerFactory by kodein.instance < PlayerFactory > ()

    suspend fun addPlayer( name: String, piece: PlayingPiece, position: SeatingPosition ): Boolean {
        val newPlayerObject = this.playerFactory.getNew( name, piece, position )
        return this.game().addPlayer( newPlayerObject )
    }

    suspend fun duplicate( action: GameAction ) {
        action.duplicate( this )
    }

    override suspend fun execute( action: GameAction ) {
        action.execute( this )
    }

    fun game(): Game {
        return this.gameState.game()
    }

    override suspend fun startGame() {
        this.gameState.setGame( this.gameFactory.newGame() )
    }

}