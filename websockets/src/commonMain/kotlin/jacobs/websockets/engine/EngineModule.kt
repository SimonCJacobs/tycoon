package jacobs.websockets.engine

import jacobs.websockets.WebSocket
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal fun < T : WebSocketParameters > getEngineModule( timestampFactory: TimestampFactory ): Kodein.Module {
    return Kodein.Module( "engine" ) {
            // TODO -> this contains state so needs to be refreshed for each socket
        bind < CommunicationCodec >() with singleton { CommunicationCodec( kodein ) }
        bind < ResponseDirector >() with singleton { ResponseDirector( kodein ) }
        bind < IncomingFrameProcessor >() with singleton { IncomingFrameProcessor( kodein ) }
        bind < JsonSerializer >() with instance( JsonSerializer() )
            // TODO -> this contains state so needs to be refreshed for each socket
        bind < MessageIdRepository >() with singleton { MessageIdRepository() }
        bind < OutgoingCommunicationDispatcher >() with
            singleton { OutgoingCommunicationDispatcher( kodein ) }
        bind < TimestampFactory >() with instance( timestampFactory )
        bind < WebSocketEngine >() with singleton { WebSocketEngine( kodein ) }
        bind < WebSocket >() with singleton { WebSocketImpl( kodein ) }
    }
}