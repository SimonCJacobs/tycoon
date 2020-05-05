package jacobs.tycoon.services

import kotlin.test.Test
import kotlin.test.asserter

class ExtensionsTest {

    @Test
    fun wrapsWhenMatchFirstElement() {
        val testList = listOf( 1, 4, 9, 10, 11, 15 )
        val expectedList = listOf( 4, 9, 10, 11, 15 )
        this.assertListsSame( expectedList, testList.wrapFromExcludingFirstMatch { it == 1 } )
    }

    @Test
    fun wrapFromExcludingFirstMatchWrapsARandomList() {
        val testList = listOf( 1, 4, 9, 10, 11, 15 )
        val expectedList = listOf( 11, 15, 1, 4, 9 )
        this.assertListsSame( expectedList, testList.wrapFromExcludingFirstMatch { it == 10 } )
    }

    private fun < T > assertListsSame( expectedList: List < T >, actualList: List < T > ) {
        expectedList.forEachIndexed {
            index, _ ->
                asserter.assertEquals( "Should be same element", expectedList[ index ], actualList[ index ] )
        }
    }

}