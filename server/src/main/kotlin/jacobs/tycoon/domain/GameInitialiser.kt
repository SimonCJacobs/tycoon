package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameInitialiser ( kodein: Kodein ) {

    private val gameControllerWrapper by kodein.instance < GameController > ( tag = "wrapper" )

    suspend fun initialiseStandardGame() {
        this.gameControllerWrapper.newGameAsync().await()
        this.gameControllerWrapper.setBoardAsync( LondonBoard() ).await()
        this.gameControllerWrapper.setPiecesAsync( ClassicPieces() ).await()
    }

}