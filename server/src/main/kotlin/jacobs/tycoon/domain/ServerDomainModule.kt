package jacobs.tycoon.domain

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun serverDomainModule(): Kodein.Module {
    return Kodein.Module ( name = "serverState" ) {
        bind < GameExecutor > ( tag = "wrapper" ) with singleton { GameExecutorWrapper( kodein ) }
        bind < GameInitialiser > () with singleton { GameInitialiser( kodein ) }
    }
}