package jacobs.jsutilities

fun Map < String, Any >.jsObject(): Any {
    val jsObject = object {}.asDynamic()
    this.forEach { jsObject[ it.key ] = it.value }
    return jsObject as Any
}

fun jsObject( closure: dynamic.() -> Unit ): Any {
    return object {}.apply( closure )
}