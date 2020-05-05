package jacobs.tycoon.state

import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.actions.AddPlayer
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.SetBoard
import jacobs.tycoon.domain.players.Position
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class UpdateCallTest {

    @Test
    fun serializesBoardCall() {
        val boardCall = SetBoard( LondonBoard() )
            .apply {
                executed = true
                successful = true
            }
        asserter.assertEquals( "Same board",
            boardCall,
            serializeAndDeserialize( boardCall )
        )
    }

    @Test
    fun serializesPlayerCall() {
        val playerCall = AddPlayer( "John", PlayingPiece( name = "Iron" ) )
            .apply {
                executed = true
                position = Position( 44 )
                successful = true
            }
        asserter.assertEquals( "Same player",
            playerCall,
            serializeAndDeserialize( playerCall )
        )
    }

    private fun serializeAndDeserialize(gameAction: GameAction): GameAction {
        val json = Json( JsonConfiguration.Stable, context = boardSerializerModule() )
        val serializedObject = json.stringify( GameAction.serializer(), gameAction )
        return json.parse( GameAction.serializer(), serializedObject )
    }

}