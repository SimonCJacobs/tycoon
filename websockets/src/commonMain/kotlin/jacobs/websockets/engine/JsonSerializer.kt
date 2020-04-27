package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import jacobs.websockets.BooleanContent
import jacobs.websockets.MessageContent
import jacobs.websockets.StringContent
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

internal class JsonSerializer {

    private val socketModule = SerializersModule {
        polymorphic( MessageContent::class ) {
            BooleanContent::class with BooleanContent.serializer()
            StringContent::class with StringContent.serializer()
        }
    }
    private val json: Json = Json( configuration = JsonConfiguration.Stable, context = socketModule )

    fun frameToMessage( frame: Frame ) : Message {
        val textFrame = frame as Frame.Text
        return json.parse( Message.serializer(), textFrame.readText() )
    }

    fun messageToFrame( message: Message ): Frame {
        return Frame.Text( json.stringify( Message.serializer(), message )  )
    }

    fun deserializeContent( serializedContent: String ): MessageContent {
        return json.parse( PolymorphicSerializer( MessageContent::class ), serializedContent )
    }

    fun serializeContent( content: MessageContent ): String {
        return json.stringify( PolymorphicSerializer( MessageContent::class ), content )
    }


}