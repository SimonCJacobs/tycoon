package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinJSUnderstanding {

    @Test @Suppress("UNUSED_VARIABLE")
    fun applyingAClosureToAnAnonymousObjectWithoutATypeDoesNotTranslateIntoJavaScript() {
        val aClosure: Any.() -> Unit = { val a = "a" }
        val aObject = object {}.aClosure()
        assertEquals( null, aObject.asDynamic().a )
    }

    @Test @Suppress("UNUSED_VARIABLE")
    fun applyingAClosureToAnAnonymousObjectWITHATypeDOESTranslateIntoJavaScript() {
        val aObject = object { val a = "a" } as Any
        val aJsObject = js( "Object.assign( {}, aObject )" )
        assertEquals( "a", aJsObject.a )
    }

}