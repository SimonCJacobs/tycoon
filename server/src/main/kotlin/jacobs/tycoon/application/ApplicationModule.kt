package jacobs.tycoon.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

internal fun applicationModule(): Kodein.Module {
    return Kodein.Module( "application" ) {
        bind < Application > () with singleton { Application( kodein ) }
        bind < CoroutineScope > () with instance( CoroutineScope( Dispatchers.IO ) )
        bind < SocketServer > () with singleton { SocketServer( kodein ) }
            // Eager to allow it to break dependency loop with SocketServer on instantiation
        bind < UpdateEngine > () with eagerSingleton { UpdateEngine( kodein ) }
    }
}