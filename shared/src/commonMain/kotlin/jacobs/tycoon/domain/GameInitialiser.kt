package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.gameadmin.SetBoard
import jacobs.tycoon.domain.actions.gameadmin.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameInitialiser ( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()

    suspend fun initialiseStandardGame( gameExecutor: GameExecutor) {
        gameExecutor.startGame()
        gameExecutor.execute(SetBoard(LondonBoard(currency)))
        gameExecutor.execute(SetPieces(ClassicPieces()))
    }

}