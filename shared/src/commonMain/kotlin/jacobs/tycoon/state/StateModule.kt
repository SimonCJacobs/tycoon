package jacobs.tycoon.state

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val stateModule = Kodein.Module( name = "state" ) {
    bind < GameState >() with singleton { GameState( kodein ) }
}