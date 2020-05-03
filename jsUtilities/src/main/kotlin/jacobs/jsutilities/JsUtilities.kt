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

/**
 * Note that this hacky reflection approach cannot handle suspend functions in the target class because it is not
 * clear how to replicate the Kotlin coroutine code that suspend functions have in the compiled JavaScript (you end
 * up with errors much like https://github.com/Kotlin/kotlinx.coroutines/issues/1145)
 */
@Suppress( "UNUSED_VARIABLE" )
fun < T : Any > KClass < T >.prototypeFunctions(): List < JsFunction > {
    val thisArgJs = this.js
    val functionList: Array < dynamic > = js( "var functionList = []; for ( var eachPropertyName in thisArgJs.prototype ) { if ( typeof thisArgJs.prototype[ eachPropertyName ] === 'function' ) functionList.push( { name: eachPropertyName, jsFunction: thisArgJs.prototype[ eachPropertyName ] } ) }; functionList;" ) as Array < dynamic >
    return functionList.map { eachDescriptor -> JsFunction( eachDescriptor.name as String, eachDescriptor.jsFunction ) }
}

@Suppress( "UNUSED_PARAMETER", "UNUSED_VARIABLE" )
internal fun < T : Any > instantiateJsClass(jsClass: JsClass < T >, constructorParameters: Array < out Any > ): T {
    val paramsWithArbitraryThisArg = arrayOf( null, *constructorParameters  )
    val instantiableJsFunction = js( "jsClass.bind.apply( jsClass, paramsWithArbitraryThisArg )" )
    return js( "new instantiableJsFunction()" ) as T
}