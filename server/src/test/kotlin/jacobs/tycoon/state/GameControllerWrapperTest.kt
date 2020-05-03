package jacobs.tycoon.state

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.GameControllerWrapper
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
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

class GameControllerWrapperTest {

    private fun getTestInstance(actualController: GameController ): GameControllerWrapper {
        val kodein = Kodein {
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
            mockitoWhen( dummyController.setBoard( londonBoard ) ).thenReturn( true )
            val instance = getTestInstance( dummyController )
            instance.setBoard( londonBoard )
            inOrder( dummyController ).apply{
                verify( dummyController ).setBoard( londonBoard )
            }
        }

    }

    @Test
    fun `Executes underlying update call when setting pieces`() {
        runBlocking {
            val dummyController = mock( GameController::class.java )
            val pieceSet = ClassicPieces()
            mockitoWhen( dummyController.setPieces( pieceSet ) ).thenReturn( true )
            val instance = getTestInstance( dummyController )
            instance.setPieces( pieceSet )
            inOrder( dummyController ).apply{
                verify( dummyController ).setPieces( pieceSet )
            }
        }

    }

}

class X : GameController {
    override suspend fun addPlayer(playerName: String, playingPiece: PlayingPiece): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun completeSignUp(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun newGame(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setBoard(board: Board): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setPieces(pieces: PieceSet): Boolean {
        TODO("Not yet implemented")
    }

}

