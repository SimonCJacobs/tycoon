package jacobs.websockets.engine

import jacobs.websockets.StringContent
import kotlin.test.Test
import kotlin.test.asserter

class SocketCommunicationTest {

    @Test
    fun createStaticMethodCreatesRequest() {
        val stringContent = StringContent( "hello" )
        val id = MessageIdentifier( "a", "b", 3 )
        val request = SocketCommunication.create( "Request", stringContent, id )
        asserter.assertEquals( "Object returned with correct content", stringContent, request.content )
        asserter.assertEquals( "Object returned with correct id", id, request.id )
    }

}