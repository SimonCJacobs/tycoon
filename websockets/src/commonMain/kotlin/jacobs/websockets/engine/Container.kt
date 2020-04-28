package jacobs.websockets.engine

import jacobs.websockets.MessageContent
import jacobs.websockets.WebSocket
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal abstract class Container < T : WebSocketParameters < T > > (
    private val application: WebSocketsApplication < T >,
    timestampFactory: TimestampFactory
) {

    protected val socketScope: SocketScope < T > = SocketScope()
    private val kodein = Kodein {
        import( engineModule() )
        import( sharedParametersModule() )
        //import( ownParametersModule() )
        bind < TimestampFactory >() with instance( timestampFactory )
    }
    private val closeSocket: ( WebSocket ) -> Unit = { application.closeSocket( it ) }

    private fun engineModule(): Kodein.Module {
        return Kodein.Module( "engine" ) {
            bind < ( WebSocket ) -> Unit > ( tag = "closeSocket" ) with singleton { closeSocket }
            bind < CommunicationCodec >() with singleton { CommunicationCodec( kodein ) }
            bind < IncomingFrameProcessor >() with scoped().singleton { IncomingFrameProcessor( kodein ) }
            bind < JsonSerializer >() with instance( JsonSerializer() )
            bind < MessageIdRepository >() with scoped().singleton { MessageIdRepository() }
            bind < OutgoingCommunicationDispatcher >() with
                scoped().singleton { OutgoingCommunicationDispatcher( kodein ) }
            bind < ResponseDirector >() with scoped().singleton { ResponseDirector( kodein ) }
            bind < WebSocket >() with scoped().singleton { WebSocketImpl( kodein ) }
            bind < WebSocketEngine >() with scoped().singleton { WebSocketEngine( kodein ) }
        }
    }

    private fun sharedParametersModule(): Kodein.Module {
        return Kodein.Module( "parameters" ) {
            bind < CoroutineScope > () with contexted().provider { context.coroutineScope }
            bind < ( MessageContent ) -> Unit > ( tag = "notification" ) with
                contexted().provider { context.notificationHandler }
            bind  < ( MessageContent ) -> MessageContent >( tag = "request" ) with
                contexted().provider { context.requestHandler }
            bind < Long >( tag = "outgoing" ) with contexted().provider { context.outgoingMessageDelay }
        }
    }

    //protected abstract fun ownParametersModule(): Kodein.Module
    protected abstract fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < T >
    protected abstract fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < T >

    fun deleteScopeRegistry( parameters: T ) {
        this.socketScope.deleteRegistry( parameters )
    }

    fun getEngine( parameters: T ): WebSocketEngine {
        return this.kodeinInContext( this.kodein, parameters )
            .direct.instance()
    }

    fun getWebSocket( parameters: T ): WebSocket {
        return this.kodeinInContext( this.kodein, parameters )
            .direct.instance()
    }

    fun prepareForNewScope( parameters: T ) {
            // Initialising the OutgoingCommunicationDispatcher ensures the ResponseDirector knows where to
            // send outgoing responses. This breaks the dependency loop that arises from the naïve implementation
        this.kodeinInContext( this.kodein, parameters ).direct.instance < OutgoingCommunicationDispatcher > ()
    }

    protected abstract fun kodeinInContext( kodein: Kodein, parameters: T ): Kodein


}