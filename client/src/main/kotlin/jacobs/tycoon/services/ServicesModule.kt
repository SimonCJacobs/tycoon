package jacobs.tycoon.services

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun servicesModule(): Kodein.Module {
    return Kodein.Module( "clientServices" ) {
        bind < ActionWriter >() with singleton { ActionWriter( kodein ) }
    }
}