package jacobs.jsutilities

import kotlin.test.Test
import kotlin.test.asserter
/*
class AbsorbWithAssign {

   // @Test
    fun absorbWithAssignAbsorbsAndAssigns() {
        val original = jsObject {
            one = "one"
            two = object { val two = "two" }
            three = object { val three = "three" }
        }
        val b = jsObject {
            two = object { val deux = "two" }
            three = object { val trois = "three" }
            four = "four"
        }
        original.absorb( b )
        val expectedObject = jsObject {
            one = "one"
            two = jsObject { two = "two"; deux = "two" }
            three = jsObject { three = "three"; trois = "three" }
            four = "four"
        }
        listOf( "one", "four" ).forEach { assertPropertySame( it, expectedObject, original ) }
        mapOf( "two" to listOf( "two", "deux" ), "three" to listOf( "three", "trois" ) )
            .entries.forEach { assertObjectPropertySame( it, expectedObject, original ) }
    }

    private fun assertPropertySame( propertyName: String, expectedObject: dynamic, actualObject: dynamic ) {
        asserter.assertEquals(
            "Property $propertyName is same",
            expectedObject[ propertyName ] as String,
            actualObject[ propertyName ] as String
        )
    }

    private fun assertObjectPropertySame( entry: Map.Entry < String, List < String > >,
                                          expectedObject: dynamic, actualObject: dynamic ) {
        entry.value.forEach { assertPropertySame(
            it,
            expectedObject[ entry.key ],
            actualObject[ entry.key ]
        ) }
    }

} */