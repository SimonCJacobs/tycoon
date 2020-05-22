package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.gameadmin.SetBoard
import jacobs.tycoon.domain.actions.gameadmin.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.cards.ShuffleOrders
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameInitialiser ( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()

    suspend fun initialiseStandardGame( gameExecutor: GameExecutor, shuffleOrders: ShuffleOrders? = null ) {
        gameExecutor.startGame()
        if ( null == shuffleOrders )
            gameExecutor.execute( SetBoard( LondonBoard( currency ) ) )
        else
            gameExecutor.execute( SetBoard( LondonBoard( currency ), shuffleOrders ) )
        gameExecutor.execute( SetPieces( ClassicPieces () ) )
    }

}