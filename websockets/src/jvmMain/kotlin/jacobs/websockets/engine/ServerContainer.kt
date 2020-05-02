package jacobs.websockets.engine

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.content.SerializationLibrary
import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.contexted as kodeinContexted
import org.kodein.di.erased.on
import org.kodein.di.erased.scoped as kodeinScoped

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ServerContainer(
    private val closeRequestHandler: CloseRequestHandler,
    private val parameters: ServerParameters
): Container < ServerParameters >() {

    override fun platformSpecificModule(): Kodein.Module {
        return Kodein.Module( "serverSpecific" ) {
            bind < CloseRequestHandler >() with instance( closeRequestHandler )
            bind < SerializationLibrary >() with instance( parameters.serializationLibrary ) // TODO try to solve strange bug here which prevents use of context
            bind < TimestampFactory >() with instance( JvmTimestampFactory() )
        }
    }

    override fun kodeinInContext( kodein: Kodein, context: SocketContext < ServerParameters > ): Kodein {
        return kodein.on( context )
    }

    override fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < SocketContext < ServerParameters > > {
        return kodeinContexted()
    }

    override fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < SocketContext < ServerParameters > > {
        return kodeinScoped( super.socketScope )
    }

}