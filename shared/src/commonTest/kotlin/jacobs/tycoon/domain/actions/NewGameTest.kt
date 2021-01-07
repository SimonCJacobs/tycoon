package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.gameadmin.NewGame
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.asserter

class NewGameTest {

    @Test
    fun newGameSerializesAndDeserializes() {
        val json = Json { serializersModule = gameActionSerializerModule() }
        val newGame = NewGame()
        val serialized = json.encodeToString( GameAction.serializer(), newGame )
        val deserialized = json.decodeFromString( GameAction.serializer(), serialized )
        asserter.assertEquals( "Should deserialize back to original", newGame::class, deserialized::class )
    }

}