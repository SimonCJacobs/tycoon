package jacobs.serialization

import kotlin.reflect.KClass

interface StoredSerializable

fun < T : StoredSerializable > T.getIdentifier(): String {
    return this::class.getIdentifier()
}

fun < T : StoredSerializable > KClass < T >.getIdentifier(): String {
    return this.simpleName!!
}