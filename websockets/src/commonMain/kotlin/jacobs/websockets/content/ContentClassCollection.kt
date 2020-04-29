package jacobs.websockets.content

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

class ContentClassCollection{

    companion object {
        fun build( closure: ContentClassCollection.() -> Unit ): ContentClassCollection {
            return ContentClassCollection().apply { closure() }
        }
    }

    private val contentClasses: MutableMap < KClass < out MessageContent>, KSerializer < out MessageContent> >
        = mutableMapOf()

    infix fun < T : MessageContent> KClass < T >.serializedBy(serializer: KSerializer < T > ) {
        contentClasses.put( this, serializer )
    }

    @Suppress( "UNCHECKED_CAST" )
    fun forEach( lambda: (KClass <MessageContent>, KSerializer <MessageContent> ) -> Unit ) {
        this.contentClasses.forEach {
            lambda( it.key as KClass <MessageContent>, it.value as KSerializer <MessageContent> )
        }
    }

}