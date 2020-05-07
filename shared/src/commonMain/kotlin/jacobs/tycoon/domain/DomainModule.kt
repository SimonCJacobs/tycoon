package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.players.PlayerFactory
import org.kodein.di.Kodein
import org.kodein.di.bindings.Singleton
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

val domainModule = Kodein.Module( name = "domain" ) {

    bind < GameController >() with singleton { GameController( kodein ) }
    bind < GameExecutor >( tag = "actual" ) with singleton { GameController( kodein ) }
    bind < GameFactory >() with singleton { GameFactory( kodein ) }
    bind < PhasePhactory >() with singleton { PhasePhactory( kodein ) }
    bind < PlayerFactory >() with singleton { PlayerFactory( kodein ) }

    bind < Currency >() with singleton { PoundsSterling() }
    bind < Int > ( tag = "goCreditAmount" ) with instance( 200 )
    bind < Int > ( tag = "initialCashCount" ) with instance( 1500 )
    bind < Int > ( tag = "toJailDoubleCount" ) with instance( 3 )

}