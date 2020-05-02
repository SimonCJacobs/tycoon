package jacobs.tycoon.clientstate

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val clientStateModule = Kodein.Module( "clientState" ) {
    bind < ClientState > () with singleton { ClientState() }
    bind < EntryPageState > () with singleton { EntryPageState() }
    bind < MainPageState > () with singleton { MainPageState() }
}