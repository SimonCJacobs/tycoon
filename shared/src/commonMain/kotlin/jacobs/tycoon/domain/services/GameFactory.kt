package jacobs.tycoon.domain.services

import jacobs.tycoon.domain.Game
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameFactory( kodein: Kodein ) {

    private val minimumNumberOfPlayers by kodein.instance < Int > ( tag = "minimumPlayers" )

    fun newGame(): Game {
        return Game(minimumNumberOfPlayers)
    }

}