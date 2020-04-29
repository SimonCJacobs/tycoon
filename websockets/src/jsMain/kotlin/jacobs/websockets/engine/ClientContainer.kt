package jacobs.websockets.engine

import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientParameters
import jacobs.websockets.content.ContentClassCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.erased.contexted as kodeinContexted
import org.kodein.di.erased.on
import org.kodein.di.erased.scoped as kodeinScoped

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientContainer(
    application: WebSocketsApplication < ClientParameters >,
    contentClasses: ContentClassCollection,
    timestampFactory: TimestampFactory
)
    : Container < ClientParameters >( application, contentClasses, timestampFactory ) {

    override fun kodeinInContext( kodein: Kodein, parameters: ClientParameters ): Kodein {
        return kodein.on( parameters )
    }

    override fun Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext < ClientParameters > {
        return kodeinContexted()
    }

    override fun Kodein.Builder.scoped(): Kodein.BindBuilder.WithScope < ClientParameters > {
        return kodeinScoped( super.socketScope )
    }

}