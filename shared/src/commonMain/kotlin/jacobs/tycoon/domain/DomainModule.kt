package jacobs.tycoon.domain

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.logs.ActionLog
import jacobs.tycoon.domain.pieces.ClassicPieces
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.GamePlayers
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val domainModule = Kodein.Module( name = "domain" ) {
    bind < ActionLog >() with singleton { ActionLog() }
    bind < Board >() with singleton { LondonBoard() }
    bind < Game >() with singleton { Game( kodein ) }
    bind < GamePlayers >() with singleton { GamePlayers() }
    bind < PieceSet >() with singleton { ClassicPieces() }
}