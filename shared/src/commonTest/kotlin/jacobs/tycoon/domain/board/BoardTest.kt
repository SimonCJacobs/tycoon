package jacobs.tycoon.domain.board

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class BoardTest {

    @Test
    fun londonBoardIsSerializable() {
        val londonBoard = LondonBoard()
        val json = Json( JsonConfiguration.Stable )
        val serializedBoard = json.stringify( LondonBoard.serializer(), londonBoard )
        val deserializedBoard = json.parse( LondonBoard.serializer(), serializedBoard )
        assertBoardsTheSame( londonBoard, deserializedBoard )
    }

    @Test
    fun londonBoardIsSerializableInScopeOfBoard() {
        val londonBoard = LondonBoard()
        val json = Json( JsonConfiguration.Stable, context = boardSerializerModule() )
        val serializedBoard = json.stringify( Board.serializer(), londonBoard )
        val deserializedBoard = json.parse( Board.serializer(), serializedBoard )
        assertBoardsTheSame( londonBoard, deserializedBoard )
    }

    private fun assertBoardsTheSame( boardOne: Board, boardTwo: Board ) {
        boardOne.squareList.indices.forEach {
            asserter.assertEquals(
                "Should return same square name",
                boardOne.squareList[ it ].name,
                boardTwo.squareList[ it ].name
            )
        }
    }

}