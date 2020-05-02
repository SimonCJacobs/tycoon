package jacobs.tycoon.state

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun serverStateModule(): Kodein.Module {
    return Kodein.Module ( name = "serverState" ) {
        bind < StateInitialiser > () with singleton { StateInitialiser( kodein ) }
        bind < StateUpdateLogWrapper > () with singleton { StateUpdateLogWrapper( kodein ) }
    }
}