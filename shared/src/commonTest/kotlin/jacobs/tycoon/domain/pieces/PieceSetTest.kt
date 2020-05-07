package jacobs.tycoon.domain.pieces

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class PieceSetTest {

    @Test
    fun classicPieceSetSerializesAndDeserializes() {
        val json = Json( JsonConfiguration.Stable, context = pieceSetSerializerModule() )
        val classicPieces = ClassicPieces()
        val serialized = json.stringify( PieceSet.serializer(), classicPieces )
        val deserialized = json.parse( PieceSet.serializer(), serialized )
        asserter.assertEquals(
            "Should deserialize back to original",
            ClassicPieces::class,
            deserialized::class
        )
    }

}