package jacobs.tycoon.domain.pieces

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.asserter

class PieceSetTest {

    @Test
    fun classicPieceSetSerializesAndDeserializes() {
        val json = Json { serializersModule = pieceSetSerializerModule() }
        val classicPieces = ClassicPieces()
        val serialized = json.encodeToString( PieceSet.serializer(), classicPieces )
        val deserialized = json.decodeFromString( PieceSet.serializer(), serialized )
        asserter.assertEquals(
            "Should deserialize back to original",
            ClassicPieces::class,
            deserialized::class
        )
    }

}