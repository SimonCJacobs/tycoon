package jacobs.websockets.engine

import jacobs.websockets.BooleanContent
import kotlin.test.Test
import kotlin.test.asserter

class JsonSerializerTest {

    private val jsonSerializer = JsonSerializer()

    @Test
    fun serializesAndDeserializesBooleanContent() {
        val booleanContent = BooleanContent( false )
        val serializedBooleanContent = this.jsonSerializer.serializeContent( booleanContent )
        val deserializedBooleanContent = this.jsonSerializer.deserializeContent( serializedBooleanContent )
        asserter.assertEquals(
            "Serializes and deserializes BooleanContent",
            booleanContent,
            deserializedBooleanContent
        )
    }

}