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

    @Test
    fun transformsInheritedProperties() {
        val aObject = Parent()
        assertEquals( "a", aObject.toJsObject().a )
        assertEquals( "basic", aObject.toJsObject().basic )
    }

    @Test
    fun transformsInheritedPropertiesInAnonymousObject() {
        val aObject = object : Base() { val a = "a" }
        assertEquals( "a", aObject.toJsObject().a )
        assertEquals( "basic", aObject.toJsObject().basic )
    }

    class A ( val a: String )
    open class Base { val basic = "basic" }
    class Parent : Base() { val a = "a" }

}