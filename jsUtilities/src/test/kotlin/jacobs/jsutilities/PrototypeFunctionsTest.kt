package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter

class PrototypeFunctionsTest {

    @Test
    fun prototypeFunctionsAllowFunctionAccessThroughReflection() {
        val firstPrototypeFn = AClass::class.prototypeFunctions()[ 0 ]
        val aClass = AClass( 10 )
        asserter.assertEquals( "Correct name", "multiply", firstPrototypeFn.name )
        asserter.assertEquals(
            "Can execute underlying function", 120, firstPrototypeFn.apply( aClass, arrayOf( 3, 4 ) )
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

    @Test
    fun prototypeFunctionsAllowAccessToSuspendFunctionsThroughReflection() {
        val firstPrototypeFn = ASuspendClass::class.prototypeFunctions()[ 0 ]
        val aSuspendClass = ASuspendClass( 10 )
        asserter.assertEquals( "Correct name", "multiplyBy5", firstPrototypeFn.name )
        asserter.assertEquals(
            "Can execute underlying function", 600, firstPrototypeFn.apply( aSuspendClass, arrayOf( 3, 4 ) )
        )
    }

    class AClass( private val c: Int ) {
        @JsName( "multiply" )
        fun multiply( a: Int, b: Int ): Int {
            return a * b * c
        }
    }

    class ASuspendClass( private val c: Int ) {
        @Suppress( "RedundantSuspendModifier" )
        @JsName( "multiplyBy5" )
        suspend fun multiplyBy5( a: Int, b: Int ): Int {
            return a * b * c * 5
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
