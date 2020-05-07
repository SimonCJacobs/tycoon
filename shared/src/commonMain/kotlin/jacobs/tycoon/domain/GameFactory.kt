package jacobs.tycoon.domain

import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.SignUp
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameFactory( kodein: Kodein ) {

    private val minimumNumberOfPlayers by kodein.instance < Int > ( tag = "minimumPlayers" )
    private val phasePhactory by kodein.instance <PhasePhactory> ()

    fun newGame(): Game {
        val game = Game( minimumNumberOfPlayers )
        game.setGamePhase( SignUp( this.phasePhactory ) )
        return game
    }

}