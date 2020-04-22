package jacobs.tycoon.network

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val networkModule = Kodein.Module( "network" ) {
    bind < Network > () with singleton { Network( kodein ) }
}
