package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter

@Suppress( "unused" )
class PrototypeFunctionsTest {

    @Test
    fun prototypeFunctionsAllowFunctionAccessThroughReflection() {
        val firstPrototypeFn = AClass::class.prototypeFunctions()[ 0 ]
        val aClass = AClass( 10 )
        asserter.assertEquals( "Correct name", "multiply", firstPrototypeFn.name )
        asserter.assertEquals(
            "Can execute underlying function", 120, firstPrototypeFn.apply( aClass, arrayOf( 3, 4 ) ) as Any
        )
    }

    @Test
    fun prototypeFunctionsAllowFunctionAccessThroughReflectionWhenNoReturnValue() {
        val firstPrototypeFn = NoReturnClass::class.prototypeFunctions()[ 0 ]
        val noReturnClass = NoReturnClass( 10 )
        asserter.assertEquals( "Correct name", "danceOnTheTable", firstPrototypeFn.name )
        firstPrototypeFn.apply( noReturnClass, arrayOf( 1 ) )
        asserter.assertEquals(
            "Can execute underlying function", 43, noReturnClass.c
        )
    }

    class AClass( private val c: Int ) {
        @JsName( "multiply" )
        fun multiply( a: Int, b: Int ): Int {
            return a * b * c
        }
    }

    class NoReturnClass( var c: Int ) {
        @Suppress("UNUSED_PARAMETER")
        @JsName( "danceOnTheTable" )
        fun danceOnTheTable( a: Int) {
            this.c += 33
        }
    }

}
