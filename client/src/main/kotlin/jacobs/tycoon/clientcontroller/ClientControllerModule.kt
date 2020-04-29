package jacobs.tycoon.clientcontroller

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun clientControllerModule(): Kodein.Module {
    return Kodein.Module( "clientController" ) {
        bind < IncomingController > () with singleton { IncomingController() }
        bind < Network > () with singleton { Network( kodein ) }
        bind < OutgoingRequestController > () with singleton { OutgoingRequestController( kodein ) }
        bind < UserInterfaceController > () with singleton { UserInterfaceController( kodein ) }
    }
}