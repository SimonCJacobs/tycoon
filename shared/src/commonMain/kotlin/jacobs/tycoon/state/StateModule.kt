package jacobs.tycoon.state

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import kotlin.math.sin

fun sharedStateModule(): Kodein.Module {
    return Kodein.Module ( name = "sharedState" ) {
        bind < ActualGameStateUpdater >() with singleton { ActualGameStateUpdater( kodein ) }
        bind < GameState >() with singleton { GameState() }
    }
}
