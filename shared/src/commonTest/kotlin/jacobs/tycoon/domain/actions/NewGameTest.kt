package jacobs.tycoon.domain.actions

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class NewGameTest {

    @Test
    fun newGameSerializesAndDeserializes() {
        val json = Json( JsonConfiguration.Stable, context = gameActionSerializerModule() )
        val newGame = NewGame()
        val serialized = json.stringify(  GameAction.serializer(), newGame )
        val deserialized = json.parse(  GameAction.serializer(), serialized )
        asserter.assertEquals( "Should deserialize back to original", newGame::class, deserialized::class )
    }

}