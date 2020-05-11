package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
//TODO port this initialiser to be fully in common code for testing purposes
class GameInitialiser ( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()
    private val gameExecutorWrapper by kodein.instance < GameExecutor > ( tag = "wrapper" )

    suspend fun initialiseStandardGame() {
        this.gameExecutorWrapper.startGame()
        this.gameExecutorWrapper.execute( SetBoard( LondonBoard( currency ) ) )
        this.gameExecutorWrapper.execute( SetPieces( ClassicPieces() ) )
    }

}