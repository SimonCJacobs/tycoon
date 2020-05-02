package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.InPlay
import jacobs.tycoon.domain.SignUp
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock

class StateUpdateLogWrapperTest {

    private fun getInstance( actualUpdater: ActualGameStateUpdater ): StateUpdateLogWrapper {
        val kodein = Kodein {
            bind < ActualGameStateUpdater > () with instance( actualUpdater )
            bind < GameHistory > () with singleton { GameHistory() }
            bind < StateUpdateLogWrapper > () with singleton { StateUpdateLogWrapper( kodein ) }
        }
        return kodein.direct.instance()
    }

    @Test
    fun `Executes underlying update call when setting Board`() {
        runBlocking {
            val dummyUpdater = mock( ActualGameStateUpdater::class.java )
            val instance = getInstance( dummyUpdater )
            val londonBoard = LondonBoard()
            instance.setBoard( londonBoard )
            inOrder( dummyUpdater ).apply{
                verify( dummyUpdater ).setBoard( londonBoard )
            }
        }

    }

    @Test
    fun `Executes underlying update call when setting pieces`() {
        runBlocking {
            val dummyUpdater = mock( ActualGameStateUpdater::class.java )
            val instance = getInstance( dummyUpdater )
            val pieceSet = ClassicPieces()
            instance.setPieces( pieceSet )
            inOrder( dummyUpdater ).apply{
                verify( dummyUpdater ).setPieces( pieceSet )
            }
        }

    }

    @Test
    fun `Executes underlying update call when setting GameStage`() {
        runBlocking {
            val dummyUpdater = mock( ActualGameStateUpdater::class.java )
            val instance = getInstance( dummyUpdater )
            instance.updateStage( InPlay() )
            instance.updateStage( SignUp() )
            inOrder( dummyUpdater ).apply{
                verify( dummyUpdater ).updateStage( InPlay() )
                verify( dummyUpdater ).updateStage( SignUp() )
            }
        }

    }

}

