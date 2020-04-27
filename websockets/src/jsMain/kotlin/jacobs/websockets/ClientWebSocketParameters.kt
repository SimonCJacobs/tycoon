package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class ClientWebSocketParameters : WebSocketParameters() {
    lateinit var hostname : String
}