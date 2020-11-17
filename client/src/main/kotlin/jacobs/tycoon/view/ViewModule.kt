package jacobs.tycoon.view

import jacobs.tycoon.application.ApplicationMode
import jacobs.tycoon.application.singletonsByMode
import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.board.DiceComponent
import jacobs.tycoon.view.components.board.squares.SquareComponentRepository
import jacobs.tycoon.view.components.board.centre.CentreCellReposifactory
import jacobs.tycoon.view.components.console.Console
import jacobs.tycoon.view.components.pages.AdminPage
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import jacobs.tycoon.view.components.pages.NoEntryPage
import jacobs.tycoon.view.components.pages.SplashPage
import jacobs.tycoon.view.components.pieces.ClassicPieceEmojiDisplayStrategy
import jacobs.tycoon.view.components.pieces.PieceComponentFactory
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import jacobs.tycoon.view.components.players.AdminPlayerComponentReposifactory
import jacobs.tycoon.view.components.players.PlayerComponentReposifactory
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.browser.document

fun viewModule( mode: ApplicationMode ) = Kodein.Module( "view" ) {

    bind < BoardComponent >() with singleton { BoardComponent( kodein ) }
    bind < CentreCellReposifactory >() with singleton { CentreCellReposifactory( kodein ) }
    bind < DiceComponent >() with singleton { DiceComponent( kodein ) }
    bind < SquareComponentRepository >() with singleton { SquareComponentRepository( kodein ) }

    bind < Console >() with singleton { Console( kodein ) }

    bind < PageWrapper >() with singleton { PageWrapper( kodein ) }
    bind < PlayerComponentReposifactory >() with singletonsByMode( mode ) {
        admin = { AdminPlayerComponentReposifactory( kodein ) }
        normal = { PlayerComponentReposifactory( kodein ) }
    }

    bind < PieceComponentFactory >() with singleton { PieceComponentFactory( kodein ) }
    bind < PieceDisplayStrategy >() with singleton { ClassicPieceEmojiDisplayStrategy() }

    bind < AdminPage >() with singleton { AdminPage( kodein ) }
    bind < EntryPage >() with singleton { EntryPage( kodein ) }
    bind < MainPage >() with singleton { MainPage( kodein ) }
    bind < NoEntryPage >() with singleton { NoEntryPage() }
    bind < SplashPage >() with singleton { SplashPage() }

    bind < HTMLElement >( tag = "main" ) with
        singleton { document.getElementsByTagName( "main" ).get( 0 ) as HTMLElement }
    bind < View >() with singleton { View( kodein ) }

}