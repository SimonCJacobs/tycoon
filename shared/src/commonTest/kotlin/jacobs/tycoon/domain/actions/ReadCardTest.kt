package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.cards.ReadCard
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class ReadCardTest {

    @Test
    fun setPiecesSerializesAndDeserializes() {
        val json = Json(
            configuration = JsonConfiguration.Stable,
            context = gameActionSerializerModule()
        )
        val readCard = ReadCard()
        val serialized = json.stringify( GameAction.serializer(), readCard )
        val deserialized = json.parse( GameAction.serializer(), serialized )
        asserter.assertEquals( "Should deserialize back to original class", ReadCard::class, deserialized::class )
    }

}