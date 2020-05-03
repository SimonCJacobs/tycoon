package jacobs.tycoon.domain

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val domainModule = Kodein.Module( name = "domain" ) {
    bind < GameController >( tag = "actual" ) with singleton { ActualGameController( kodein ) }
    bind < GameFactory >() with singleton { GameFactory( kodein ) }
}