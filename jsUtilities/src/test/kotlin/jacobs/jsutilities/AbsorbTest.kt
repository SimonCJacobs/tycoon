package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter

class AbsorbTest {

    @Test
    fun absorbAbsorbs() {
        val original = jsObject {
            one = "one"
            two = "two"
        }
        val b = jsObject {
            three = "three"
            four = "four"
        }
        original.absorb( b )
        val expectedObject = jsObject {
            one = "one"
            two = "two"
            three = "three"
            four = "four"
        }
        listOf( "one", "two", "three", "four" ).forEach { assertPropertySame( it, expectedObject, original ) }
    }

    private fun assertPropertySame( propertyName: String, expectedObject: dynamic, actualObject: dynamic ) {
        asserter.assertEquals(
            "Property $propertyName is same",
            expectedObject[ propertyName ] as Any,
            actualObject[ propertyName ] as Any
        )
    }

}