package jacobs.tycoon.clientstate

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val clientStateModule = Kodein.Module( "clientState" ) {
    bind < ClientState > () with singleton { ClientState() }
}