package jacobs.websockets.engine

import jacobs.websockets.content.MessageContent
import jacobs.websockets.WebSocket
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.bindings.Provider
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal abstract class Container < T : WebSocketParameters < T > > {

    protected val socketScope: SocketScope < T, SocketContext < T > > = SocketScope()
    private val kodein by lazy { // Lazy to allow super class to initialise
        Kodein {
            import( engineModule() )
            import( sharedParametersModule() )
            import( platformSpecificModule() )
        }
    }

    // MODULE COMPOSITION METHODS

    /**
     * Platform must bind CloseRequestHandler, ContentClassCollection and TimestampFactory
     */
    protected abstract fun platformSpecificModule(): Kodein.Module

    private fun engineModule(): Kodein.Module {
        return Kodein.Module( "engine" ) {
            bind < CommunicationCodec >() with singleton { CommunicationCodec( kodein ) }
            bind < IncomingFrameProcessor >() with scoped().singleton { IncomingFrameProcessor( kodein ) }
                // Eager because it registers MessageContent classes as serializable, and those must be
                // serializable before JsonSerializer itself is used TODO: WHY??
            bind < JsonSerializer >() with eagerSingleton { JsonSerializer( kodein ) }
            bind < MessageIdRepository >() with scoped().singleton { MessageIdRepository() }
            bind < OutgoingCommunicationDispatcher >() with
                scoped().singleton { OutgoingCommunicationDispatcher( kodein ) }
            bind < OutgoingFrameProducer >() with scoped().singleton { OutgoingFrameProducer( kodein ) }
            bind < ResponseDirector >() with scoped().singleton { ResponseDirector( kodein ) }
            bind < WebSocket >() with scoped().singleton { WebSocketImpl( kodein ) }
            bind < WebSocketEngine >() with scoped().singleton { WebSocketEngine( kodein ) }
        }
    }

    private fun sharedParametersModule(): Kodein.Module {
        return Kodein.Module( "parameters" ) {
            bind < CoroutineScope > () with parameter { coroutineScope }
            bind < suspend ( MessageContent ) -> Unit > ( tag = "notification" ) with
                parameter { notificationHandler }
            bind  < suspend ( MessageContent ) -> MessageContent > ( tag = "request" ) with
                parameter { requestHandler }
            bind < Long > ( tag = "outgoing" ) with parameter { outgoingMessageDelay }
        }
    }

    protected inline fun < reified S : Any > Kodein.Builder.parameter( crossinline parameterAccessor: T.() -> S ): Provider < SocketContext < T >, S > {
        return contexted().provider { context.parameters.parameterAccessor() }
    }

    protected abstract fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < SocketContext < T > >
    protected abstract fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < SocketContext < T > >

    // PUBLIC API

    fun deleteScopeRegistry( context: SocketContext < T > ) {
        this.socketScope.deleteRegistry( context )
    }

    fun getEngineInContext( context: SocketContext < T > ): WebSocketEngine {
        return this.kodeinInContext( this.kodein, context )
            .direct.instance()
    }

    fun getWebSocketInContext( context: SocketContext < T > ): WebSocket {
        return this.kodeinInContext( this.kodein, context )
            .direct.instance()
    }

    fun prepareForNewScope( context: SocketContext < T > ) {
            // Initialising the OutgoingCommunicationDispatcher ensures the ResponseDirector knows where to
            // send outgoing responses. This breaks the dependency loop that arises from the naïve implementation
        this.kodeinInContext( this.kodein, context ).direct.instance < OutgoingCommunicationDispatcher > ()
    }

    protected abstract fun kodeinInContext( kodein: Kodein, context: SocketContext < T > ): Kodein

}