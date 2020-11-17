package jacobs.websockets.engine

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import jacobs.websockets.content.SerializationLibrary
import jacobs.websockets.content.MessageContent
import jacobs.websockets.content.getWebSocketContentClasses
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

internal class JsonSerializer( kodein: Kodein ) {

    private val socketModule = SerializersModule {
        polymorphic( MessageContent::class ) {
            getWebSocketContentClasses().forEachContentClass {
                clazz, serializer -> addSubclass( clazz, serializer )
            }
            serializationLibrary( kodein ).forEachContentClass {
                clazz, serializer -> addSubclass( clazz, serializer )
            }
        }
        serializationLibrary( kodein ).forEachSerialModule { include( it ) }
    }
    private val json: Json = Json( configuration = JsonConfiguration.Stable, context = socketModule )

    private fun serializationLibrary( kodein: Kodein ): SerializationLibrary {
        return kodein.direct.instance()
    }

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