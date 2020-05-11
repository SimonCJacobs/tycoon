package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.assertEquals

class JsObjectTest {

    @Test
    fun transformsAnonymousObject() {
        val aObject = object { val a = "a" }
        assertEquals( "a", aObject.toJsObject().a )
    }

    @Test
    fun transformsRegularClass() {
        val aObject = A( "a" )
        assertEquals( "a", aObject.toJsObject().a )
    }

    class A ( val a: String )

}