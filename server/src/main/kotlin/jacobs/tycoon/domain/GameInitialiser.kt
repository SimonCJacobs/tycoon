package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.NewGame
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameInitialiser ( kodein: Kodein ) {

    private val gameControllerWrapper by kodein.instance < GameExecutor > ( tag = "wrapper" )

    suspend fun initialiseStandardGame() {
        this.gameControllerWrapper.execute( NewGame() )
        this.gameControllerWrapper.execute( SetBoard( LondonBoard() ) )
        this.gameControllerWrapper.execute( SetPieces( ClassicPieces() ) )
    }

}