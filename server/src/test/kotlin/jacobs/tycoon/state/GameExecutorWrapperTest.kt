package jacobs.tycoon.state

import jacobs.tycoon.domain.GameExecutor
import jacobs.tycoon.domain.GameExecutorWrapper
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.actions.SetPieces
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.pieces.ClassicPieces
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
class GameExecutorWrapperTest {

    private fun getTestInstance(actualExecutor: GameExecutor ): GameExecutorWrapper {
        val kodein = Kodein {
            bind < CoroutineScope > () with instance( CoroutineScope( Dispatchers.Default ) )
            bind < GameExecutor > ( tag = "actual" ) with instance( actualExecutor )
            bind < GameExecutorWrapper > () with singleton { GameExecutorWrapper( kodein ) }
            bind < GameHistory > () with singleton { GameHistory() }
        }
        return kodein.direct.instance()
    }

    @Test
    fun `Executes underlying update call when setting Board`() {
        runBlocking {
            val dummyController = mock( GameExecutor::class.java )
            val londonBoardAction = SetBoard( LondonBoard( PoundsSterling() ) )
            mockitoWhen( dummyController.execute( londonBoardAction ) )
                .then {
                    londonBoardAction.executed = true
                    londonBoardAction
                }
            val instance = getTestInstance( dummyController )
            instance.execute( londonBoardAction )
            inOrder( dummyController ).verify( dummyController )
                .execute( londonBoardAction )
        }

    }

    @Test
    fun `Executes underlying update call when setting pieces`() {
        runBlocking {
            val dummyController = mock( GameExecutor::class.java )
            val pieceSetAction = SetPieces( ClassicPieces() )
            mockitoWhen( dummyController.execute( pieceSetAction ) )
                .then {
                    pieceSetAction.executed = true
                    pieceSetAction
                }
            val instance = getTestInstance( dummyController )
            instance.execute( pieceSetAction )
            inOrder( dummyController )
                .verify( dummyController )
                .execute( pieceSetAction )
        }

    }

}

