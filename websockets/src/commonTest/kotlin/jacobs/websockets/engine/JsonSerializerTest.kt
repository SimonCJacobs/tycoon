package jacobs.websockets.engine

import jacobs.websockets.content.BooleanContent
import jacobs.websockets.content.SerializationLibrary
import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import kotlin.test.Test
import kotlin.test.asserter

class JsonSerializerTest {

    private lateinit var jsonSerializer: JsonSerializer

    private fun useSimpleInstance() {
        this.useWithContentClassCollection( SerializationLibrary() )
    }

    private fun useWithContentClassCollection( content: SerializationLibrary ) {
        val kodein = Kodein {
            bind < SerializationLibrary > () with singleton { content }
            bind < JsonSerializer > () with singleton { JsonSerializer( kodein ) }
        }
        this.jsonSerializer = kodein.direct.instance()
    }

    @Test
    fun serializesAndDeserializesBooleanContent() {
        this.useSimpleInstance()
        val booleanContent = BooleanContent( false )
        val serializedBooleanContent = this.jsonSerializer.serializeContent( booleanContent )
        val deserializedBooleanContent = this.jsonSerializer.deserializeContent( serializedBooleanContent )
        asserter.assertEquals(
            "Serializes and deserializes BooleanContent",
            booleanContent,
            deserializedBooleanContent
        )
    }

    @Test
    fun serializesAndDeserializesUserDefinedClass() {
        val content = SerializationLibrary.build {
            SimpleRequestWrapper::class serializedBy SimpleRequestWrapper.serializer()
        }
        this.useWithContentClassCollection( content )
        val wrapper = SimpleRequestWrapper( AnEnum.NUMB_THE_ENUM )
        val serialized = this.jsonSerializer.serializeContent( wrapper )
        val deserialized = this.jsonSerializer.deserializeContent( serialized ) as SimpleRequestWrapper
        asserter.assertEquals( "property same", wrapper.identifier, deserialized.identifier )
    }

}

@Serializable
class SimpleRequestWrapper(
    val identifier: AnEnum
) : MessageContent {
    fun aFunFunction(): String {
        return "fun"
    }
}

enum class AnEnum {
    NUMB_THE_ENUM
}