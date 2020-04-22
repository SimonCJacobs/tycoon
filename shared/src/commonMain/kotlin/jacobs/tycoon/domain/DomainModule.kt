package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.logs.ActionLog
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val domainModule = Kodein.Module( name = "domain" ) {
    bind < Game >() with singleton { Game( kodein ) }
    bind < GameStateProvider >() with singleton { GameStateProvider( kodein ) }
    bind < ActionLog >() with singleton { ActionLog() }
    bind < Board >() with singleton { LondonBoard() }
}