package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.pieces.ClassicPieces
import jacobs.tycoon.domain.pieces.pieceSetSerializerModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.plus
import kotlin.test.Test
import kotlin.test.asserter

class SetPiecesTest {

    @Test
    fun setPiecesSerializesAndDeserializes() {
        val json = Json(
            configuration = JsonConfiguration.Stable,
            context = gameActionSerializerModule() + pieceSetSerializerModule()
        )
        val setPieces = SetPieces( ClassicPieces() )
        val serialized = json.stringify( GameAction.serializer(), setPieces )
        val deserialized = json.parse( GameAction.serializer(), serialized )
        asserter.assertEquals( "Should deserialize back to original class", SetPieces::class, deserialized::class )
        asserter.assertEquals(
            "Should deserialize back to original with properties",
            ClassicPieces::class,
            ( deserialized as SetPieces ).pieceSet::class
        )
    }

}