package jacobs.tycoon.state

import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class UpdateCallTest {

    @Test
    fun serializesBoardCall() {
        val boardCall = BoardCall( LondonBoard() ).apply { functionName = "foo" }
        asserter.assertEquals( "Same player",
            boardCall.run { singleArg()().squareList[ 0 ].name },
            ( serializeAndDeserialize( boardCall ) as BoardCall ).run { singleArg()().squareList[ 0 ].name }
        )
    }

    @Test
    fun serializesPlayerCall() {
        val playerCall = PlayerCall( Player( "John", PlayingPiece( name = "Iron" ) ) ).apply { functionName = "foo" }
        asserter.assertEquals( "Same player",
            playerCall.run { singleArg()().name },
            ( serializeAndDeserialize( playerCall ) as PlayerCall ).run { singleArg()().name }
        )
    }

    private fun serializeAndDeserialize( updateCall: UpdateCall ): UpdateCall {
        val json = Json( JsonConfiguration.Stable, context = boardSerializerModule() )
        val serializedObject = json.stringify( UpdateCall.serializer(), updateCall )
        return json.parse( UpdateCall.serializer(), serializedObject )
    }

}