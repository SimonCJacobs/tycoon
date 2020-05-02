package jacobs.websockets.content

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerialModule
import kotlin.reflect.KClass

class SerializationLibrary {

    companion object {
        fun build( closure: SerializationLibrary.() -> Unit ): SerializationLibrary {
            return SerializationLibrary().apply { closure() }
        }
    }

    private val contentClasses: MutableMap < KClass < out MessageContent>, KSerializer < out MessageContent> >
        = mutableMapOf()

    private val serialModules: MutableList < SerialModule > = mutableListOf()

    // INCOMING API

    fun serialModule( module: SerialModule ) {
        this.serialModules.add( module )
    }

    infix fun < T : MessageContent > KClass < T >.serializedBy( serializer: KSerializer < T > ) {
        contentClasses.put( this, serializer )
    }

    // OUTGOING API

    @Suppress( "UNCHECKED_CAST" )
    fun forEachContentClass( lambda: ( KClass < MessageContent >, KSerializer < MessageContent > ) -> Unit ) {
        this.contentClasses.forEach {
            lambda( it.key as KClass <MessageContent>, it.value as KSerializer <MessageContent> )
        }
    }

    fun forEachSerialModule( lambda: ( SerialModule ) -> Unit ) {
        this.serialModules.forEach( lambda )
    }

}