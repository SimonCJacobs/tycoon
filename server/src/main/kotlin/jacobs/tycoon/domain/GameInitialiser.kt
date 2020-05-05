package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameInitialiser ( kodein: Kodein ) {

    private val gameExecutorWrapper by kodein.instance < GameExecutor > ( tag = "wrapper" )

    suspend fun initialiseStandardGame() {
        this.gameExecutorWrapper.startGame()
        this.gameExecutorWrapper.execute( SetBoard( LondonBoard() ) )
        this.gameExecutorWrapper.execute( SetPieces( ClassicPieces() ) )
    }

}