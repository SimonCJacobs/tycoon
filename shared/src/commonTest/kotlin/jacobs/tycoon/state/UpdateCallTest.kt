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
        val boardCall = SetBoard( LondonBoard() )
        asserter.assertEquals( "Same player",
            boardCall,
            serializeAndDeserialize( boardCall )
        )
    }

    @Test
    fun serializesPlayerCall() {
        val playerCall = AddPlayer( Player( "John", PlayingPiece( name = "Iron" ) ) )
        asserter.assertEquals( "Same player",
            playerCall,
            serializeAndDeserialize( playerCall )
        )
    }

    private fun serializeAndDeserialize(gameUpdate: GameUpdate ): GameUpdate {
        val json = Json( JsonConfiguration.Stable, context = boardSerializerModule() )
        val serializedObject = json.stringify( GameUpdate.serializer(), gameUpdate )
        return json.parse( GameUpdate.serializer(), serializedObject )
    }

}