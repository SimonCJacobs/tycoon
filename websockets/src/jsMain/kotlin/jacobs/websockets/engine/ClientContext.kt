package jacobs.websockets.engine

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientContext(
    parameters: ClientParameters
) : SocketContext < ClientParameters >( parameters )