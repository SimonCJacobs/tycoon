package jacobs.websockets.engine

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.contexted as kodeinContexted
import org.kodein.di.erased.on
import org.kodein.di.erased.scoped as kodeinScoped

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ServerContainer(
    application: WebSocketsApplication < ServerParameters >,
    timestampFactory: TimestampFactory
)
    : Container < ServerParameters >( application, timestampFactory ) {

    override fun kodeinInContext( kodein: Kodein, parameters: ServerParameters ): Kodein {
        return kodein.on( parameters )
    }

    override fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < ServerParameters > {
        return kodeinContexted()
    }

    override fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < ServerParameters > {
        return kodeinScoped( super.socketScope )
    }

}