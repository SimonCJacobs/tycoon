package jacobs.tycoon.application

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

internal fun applicationModule(): Kodein.Module {
    return Kodein.Module( "application") {
        bind < SocketListener > () with singleton { SocketListener( kodein ) }
    }
}