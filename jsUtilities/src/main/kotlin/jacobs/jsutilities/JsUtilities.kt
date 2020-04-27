package jacobs.jsutilities

import kotlin.reflect.KClass

fun Map < String, Any >.jsObject(): Any {
    val jsObject = object {}.asDynamic()
    this.forEach { jsObject[ it.key ] = it.value }
    return jsObject as Any
}

fun jsObject(): Any {
    return object {}
}

fun jsObject( closure: dynamic.() -> Unit ): Any {
    return object {}.apply( closure )
}

fun < T : Any > KClass < T >.createInstance( vararg constructorParameters: Any ): T {
    return instantiateJsClass( this.js, constructorParameters )
}

@Suppress( "UNUSED_PARAMETER", "UNUSED_VARIABLE" )
internal fun < T : Any > instantiateJsClass(jsClass: JsClass < T >, constructorParameters: Array < out Any > ): T {
    val paramsWithArbitraryThisArg = arrayOf( null, *constructorParameters  )
    val instantiableJsFunction = js( "jsClass.bind.apply( jsClass, paramsWithArbitraryThisArg )" )
    return js( "new instantiableJsFunction()" ) as T
}