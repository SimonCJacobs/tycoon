package jacobs.serialization

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

/**
 * Restricted somewhat by the very limited reflection capacity of Kotlin/JS at the time of writing (25.4.20)
 */
object SerializerStore {

    private val serializers: MutableMap < String, KSerializer < out StoredSerializable > > = mutableMapOf()

    init {
        this.addSerializer( StoredString::class, StoredString.serializer() )
    }

    fun < T : StoredSerializable > addSerializer( clazz: KClass < T >, serializer: KSerializer < T > ) {
        this.serializers.put( clazz.getIdentifier(), serializer )
    }

        // TODO: consider if would like to wrap this in an object so not entirely global
    fun getByIdentifier( identifier: String ): KSerializer < out StoredSerializable > {
            println( "store access using identifier $identifier")
        if ( this.serializers.containsKey( identifier ) )
            return this.serializers.getValue( identifier )
        else
            throw Error( "Attempted to obtain serializer for unknown class $identifier" )
    }

}