package jacobs.tycoon.clientcontroller

import jacobs.tycoon.services.Network
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun clientControllerModule(): Kodein.Module {
    return Kodein.Module( "clientController" ) {
        bind < EntryPageController > () with singleton { EntryPageController( kodein ) }
        bind < IncomingController > () with singleton { IncomingController( kodein ) }
        bind < MainPageController > () with singleton { MainPageController( kodein ) }
        bind < OutgoingRequestController > () with singleton { OutgoingRequestController( kodein ) }
        bind < StateSynchroniser > () with singleton { StateSynchroniser( kodein ) }
        bind < UserInterfaceController > () with singleton { UserInterfaceController( kodein ) }
    }
}