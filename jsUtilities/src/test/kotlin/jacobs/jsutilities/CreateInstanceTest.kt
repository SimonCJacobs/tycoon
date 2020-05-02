package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter

class CreateInstanceTest {

    @Test
    fun instantiateClassWithoutParameters() {
        class Foo {
            fun foo(): String { return "it's me" }
        }
        val output = Foo::class.createInstance().foo()
        asserter.assertEquals( "Should output as foo", "it's me", output )
    }

    @Test
    fun instantiateClassWithParameters() {
        class Foo( private val a: String, private val b: String ) {
            fun foo(): String { return "it's me again $a $b" }
        }
        val output = Foo::class.createInstance( "yes", "really" ).foo()
        asserter.assertEquals( "Should output as foo", "it's me again yes really", output )
    }

    @Test
    fun instantiateClassUsingPrimaryConstructor() {
        class Foo constructor( val a: String ) {
            constructor( b: Int ) : this( "$b the second" )
        }
        val instance = Foo::class.createInstance( "Henry" )
        asserter.assertEquals( "Should instantiate primary constructor", "Henry", instance.a )
    }

}