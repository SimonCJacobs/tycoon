package jacobs.tycoon.state

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.GameControllerWrapper
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen

@Suppress( "DeferredResultUnused" )
class GameControllerWrapperTest {

    private fun getTestInstance( actualController: GameController ): GameControllerWrapper {
        val kodein = Kodein {
            bind < CoroutineScope > () with instance( CoroutineScope( Dispatchers.Default ) )
            bind < GameController > ( tag = "actual" ) with instance( actualController )
            bind < GameControllerWrapper > () with singleton { GameControllerWrapper( kodein ) }
            bind < GameHistory > () with singleton { GameHistory() }
        }
        return kodein.direct.instance()
    }

    @Test
    fun `Executes underlying update call when setting Board`() {
        runBlocking {
            val dummyController = mock( GameController::class.java )
            val londonBoard = LondonBoard()
            mockitoWhen( dummyController.setBoardAsync( londonBoard ) )
                .thenReturn( CompletableDeferred( true ) )
            val instance = getTestInstance( dummyController )
            instance.setBoardAsync( londonBoard ).await()
            inOrder( dummyController ).verify( dummyController )
                .setBoardAsync( londonBoard )
        }

    }

    @Test
    fun `Executes underlying update call when setting pieces`() {
        runBlocking {
            val dummyController = mock( GameController::class.java )
            val pieceSet = ClassicPieces()
            mockitoWhen( dummyController.setPiecesAsync( pieceSet ) )
                .thenReturn( CompletableDeferred( true ) )
            val instance = getTestInstance( dummyController )
            instance.setPiecesAsync( pieceSet ).await()
            inOrder( dummyController )
                .verify( dummyController )
                .setPiecesAsync( pieceSet )
        }

    }

}

