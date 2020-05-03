package jacobs.tycoon.servercontroller

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

internal fun serverControllerModule(): Kodein.Module {
    return Kodein.Module( "serverController" ) {
        bind < FrontController > () with singleton { FrontController( kodein ) }
    }
}