package jacobs.tycoon.application

import jacobs.tycoon.ApplicationSettings
import jacobs.websockets.StringContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import jacobs.websockets.WebSockets

@ExperimentalCoroutinesApi
suspend fun main() {
    WebSockets().websocketServer {
        path = ApplicationSettings.SOCKET_PATH
        port = ApplicationSettings.SOCKET_PORT
        requestHandler = {
            content ->
                val stringContent = content as StringContent
                StringContent( "back atcha " + stringContent.string )
        }
    }
}