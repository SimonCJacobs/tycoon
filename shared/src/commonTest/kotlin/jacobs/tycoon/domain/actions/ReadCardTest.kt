package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.asserter

class ReadCardTest {

    @Test
    fun readCardSerializesAndDeserializes() {
        val json = Json {
            serializersModule = gameActionSerializerModule()
        }
        val readCard = ReadCard( SeatingPosition.UNASSIGNABLE )
        val serialized = json.encodeToString( PositionalGameAction.serializer(), readCard )
        val deserialized = json.decodeFromString( PositionalGameAction.serializer(), serialized )
        asserter.assertEquals( "Should deserialize back to original class", ReadCard::class, deserialized::class )
    }

}