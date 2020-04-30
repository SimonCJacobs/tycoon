package jacobs.websockets.engine

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientParameters
import jacobs.websockets.WebSocket
import jacobs.websockets.content.ContentClassCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.contexted as kodeinContexted
import org.kodein.di.erased.on
import org.kodein.di.erased.singleton
import org.kodein.di.erased.scoped as kodeinScoped

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientContainer(
    private val closeRequestHandler: CloseRequestHandler,
    private val contentClasses: ContentClassCollection
)
    : Container < ClientParameters >() {

    override fun platformSpecificModule(): Kodein.Module {
        return Kodein.Module( "clientSpecific" ) {
            bind < CloseRequestHandler >() with instance( closeRequestHandler )
            bind < ContentClassCollection >() with instance( contentClasses )
            bind < TimestampFactory >() with instance( JsTimestampFactory() )
        }
    }

    override fun kodeinInContext( kodein: Kodein, context: SocketContext < ClientParameters > ): Kodein {
        return kodein.on( context )
    }

    override fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < SocketContext < ClientParameters > > {
        return kodeinContexted()
    }

    override fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < SocketContext < ClientParameters > > {
        return kodeinScoped( super.socketScope )
    }

}