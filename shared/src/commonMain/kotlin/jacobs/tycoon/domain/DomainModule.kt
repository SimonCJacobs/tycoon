package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.players.PlayerFactory
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.rules.StandardJailRules
import jacobs.tycoon.domain.services.GameCycle
import jacobs.tycoon.domain.services.GameFactory
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun domainModule(): Kodein.Module {
    val actualGameExecutor = ActualGameExecutor()
    return Kodein.Module( name = "domain" ) {
        /**
         * ActualGameExecutor is bound twice so that (i) GameController can access its non-interface methods,
         * but also (ii) that it can still be bound via its interface. As it contains no dependencies or state
         * at the time of writing (12.5.20), that is not a problem.
         */
        bind < ActualGameExecutor >() with instance( actualGameExecutor )
        bind < GameExecutor >( tag = "actual" ) with  instance( actualGameExecutor )

        bind < GameController >() with singleton { GameController( kodein ) }
        bind < GameCycle > () with singleton { GameCycle( kodein ) }
        bind < GameFactory >() with singleton { GameFactory( kodein ) }
        bind < GameInitialiser > () with singleton { GameInitialiser( kodein ) }
        bind < PhasePhactory >() with singleton { PhasePhactory( kodein ) }
        bind < PlayerFactory >() with singleton { PlayerFactory( kodein ) }

        bind < Currency >() with singleton { PoundsSterling() }
        bind < JailRules >() with singleton { StandardJailRules( kodein ) }
        bind < MiscellaneousRules > () with singleton { MiscellaneousRules( kodein ) }
    }
}
