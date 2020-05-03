package jacobs.tycoon.domain

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun serverDomainModule(): Kodein.Module {
    return Kodein.Module ( name = "serverState" ) {
        bind < GameController > ( tag = "wrapper" ) with singleton { GameControllerWrapper( kodein ) }
        bind < GameInitialiser > () with singleton { GameInitialiser( kodein ) }
    }
}