package jacobs.tycoon.state

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun sharedStateModule(): Kodein.Module {
    return Kodein.Module ( name = "sharedState" ) {
        bind < GameHistory >() with singleton { GameHistory() }
        bind < GameState >() with singleton { GameState() }
    }
}