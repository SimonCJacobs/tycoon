package jacobs.tycoon.state

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.board.Board

class GameState {

    private var maybeGame: Game? = null

    fun game(): Game {
        if ( maybeGame == null )
            throw Error( "Game not initialised" )
        else
            return maybeGame!!
    }

    fun hasGame(): Boolean {
        return maybeGame !== null
    }

    fun setGame( game: Game ) {
        this.maybeGame = game
    }

}
