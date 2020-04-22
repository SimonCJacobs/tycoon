package jacobs.tycoon.clientstate

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton

val clientStateModule = Kodein.Module( "clientState" ) {
    bind < ClientState > () with eagerSingleton { ClientState() }
}