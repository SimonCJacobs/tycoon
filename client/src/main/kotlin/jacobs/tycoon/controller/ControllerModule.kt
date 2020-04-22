package jacobs.tycoon.controller

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val controllerModule = Kodein.Module( "controller" ) {
    bind < MainController >() with singleton { MainController( kodein ) }
    bind < UserInterfaceController >() with singleton { UserInterfaceController( kodein ) }
}