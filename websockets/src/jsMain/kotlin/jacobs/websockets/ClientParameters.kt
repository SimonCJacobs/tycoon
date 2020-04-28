package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ClientParameters : WebSocketParameters < ClientParameters > () {
    lateinit var hostname : String
}