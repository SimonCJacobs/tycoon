package jacobs.tycoon.application

import jacobs.tycoon.ApplicationSettings
import jacobs.websockets.StringContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import jacobs.websockets.WebSockets
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
fun main( args: Array < String > ) {
    runBlocking {
        WebSockets()
            .websocketServer {
                coroutineScope = CoroutineScope( Dispatchers.IO )
                path = ApplicationSettings.SOCKET_PATH
                port = ApplicationSettings.SOCKET_PORT
                requestHandler = {
                    content ->
                        val stringContent = content as StringContent
                        StringContent( "back atcha " + stringContent.string )
                }
            }
    }
Thread.sleep(100000)
}