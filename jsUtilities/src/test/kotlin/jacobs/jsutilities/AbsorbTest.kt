package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter

class AbsorbTest {

    @Test @Suppress( "UNUSED_VARIABLE" )
    fun absorbAbsorbs() {
        val original = js( "{ one: \"one\", two: \"two\" }" )
        val b = object {
            val three = "three"
            val four = "four"
        }
        absorb( original, b )
        val expectedObject = js( "{ one: \"one\", two: \"two\", three: \"three\", four: \"four\"}" )
        listOf( "one", "two", "three", "four" ).forEach { assertPropertySame( it, expectedObject, original ) }
    }

    @Suppress( "USELESS_CAST" )
    private fun assertPropertySame(propertyName: String, expectedObject: dynamic, actualObject: Any ) {
        asserter.assertEquals(
            "Property $propertyName is same",
            expectedObject[ propertyName ] as Any?,
            actualObject.asDynamic()[ propertyName ] as Any?
        )
    }

}